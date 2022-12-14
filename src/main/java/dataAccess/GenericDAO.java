package dataAccess;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.logging.Logger;

import connection.ConnectionFactory;

public class GenericDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(GenericDAO.class.getName());
    private final Class<T> type;
    private Object field;

    @SuppressWarnings("unchecked")
    public GenericDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Searches for an entry in the database based on the key parameter
     * @return: object found of type T.
     */
    public T find(Object key) {
        Connection dbConnection = ConnectionFactory.getConnection();
        T toReturn = null;
        PreparedStatement findStatement = null;
        String findQuery = null;
        if(key.getClass().getSimpleName().equals("Integer")) {
            findQuery = findQuery("ID");
        }
        else if(key.getClass().getSimpleName().equals("String")) {
            findQuery = findQuery("email");
        } else {
            return null;
        }

        ResultSet resultSet = null;
        try {
            findStatement = dbConnection.prepareStatement(findQuery);

            if((key.getClass().getSimpleName()).equals("Integer")) {
                findStatement.setInt(1, (Integer) key);
            }
            else {
                findStatement.setString(1, (String) key);
            }

            resultSet = findStatement.executeQuery();
            toReturn = retrieveObject(resultSet); // retrieve object

        } catch (SQLException e) {
            toReturn = null;
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(findStatement);
            ConnectionFactory.close(dbConnection);
        }
        return toReturn;
    }

    /**
     * Inserts a new row in the database based on the type of object passed as
     * parameter.
     * @param toInsert
     * @return the ID of the newly inserted object (generated by the database)
     */
    public int insert(Object toInsert) {
        Connection dbConnection = ConnectionFactory.getConnection();
        int insertedID = -1; //return value
        PreparedStatement insertStatement = null;

        try{
            insertStatement = dbConnection.prepareStatement(insertQuery(), Statement.RETURN_GENERATED_KEYS);
            int paramIndex = setFields(insertStatement, toInsert);
            insertStatement.executeUpdate();

            ResultSet rs = insertStatement.getGeneratedKeys();
            if (rs.next()) {
                insertedID = rs.getInt(1);
            }
        } catch (Exception e) {
            return -3;
        } finally {
            ConnectionFactory.close(insertStatement);
            ConnectionFactory.close(dbConnection);
        }

        return insertedID;
    }

    /**
     * Edits an entry in the databased found based on a key. The blueprint of the
     * edited entry is provided by the parameter object.
     * @param object
     * @param key
     * @return
     */
    public int edit(Object object, Object key) {
        int status = 0;
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement editStatement = null;

        try {
            Field updateKeyField = getField(object, key);
            editStatement = dbConnection.prepareStatement(editQuery(updateKeyField));
            int paramIndex = setFields(editStatement, object);
            editStatement.setObject(paramIndex, key);

            editStatement.execute();

        } catch (Exception e) {
            return -1;
        } finally {
            ConnectionFactory.close(editStatement);
            ConnectionFactory.close(dbConnection);
        }
        return status;
    }

    /**
     * Deletes an object from the database based on the key given as parameter
     * @param key
     * @return
     */
    public int delete(Object key) {
        int status = 0;;
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement deleteStatement = null;
        try {
            Object objectFound = find(key);
            Field deleteKeyField = getField(objectFound, key);
            deleteStatement = dbConnection.prepareStatement(deleteQuery(deleteKeyField));
            deleteStatement.setObject(1, key);
            deleteStatement.execute();

        } catch (SQLException e) {
            status = -1;
        } finally {
            ConnectionFactory.close(deleteStatement);
            ConnectionFactory.close(dbConnection);
        }
        return status;
    }

    /**
     * Retrieves the entire data from a database table and returns it as a String[][]
     * variable. The data is destined to be shown in a JTable in the presentation layer.
     * @return
     */
    public String[][] showAll() {
        int nrColumns = this.numberOfEntries();
        String[][] data = new String[nrColumns][6];
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement showStatement = null;
        ResultSet rs = null;
        try {
            showStatement = dbConnection.prepareStatement(showQuery());
            rs = showStatement.executeQuery();
            Field[] fields = type.getDeclaredFields();

            int i = 0;
            while (rs.next()) {

                for(int j = 0; j < fields.length; j++) {
                    if (fields[j].getType().toString().equals("int")) {
                        data[i][j] = Integer.toString((Integer) rs.getObject(fields[j].getName()));
                    } else {
                        data[i][j] = (String) rs.getObject(fields[j].getName());
                    }
                }
                i++;
            }
        } catch (SQLException e) {
            data = null;
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(showStatement);
            ConnectionFactory.close(dbConnection);
        }
        return data;
    }

    /**
     * Returns the number of rows in the database table.
     * Uses the table specified in the type field of the GenericDAO class.
     * @return number of rows as an int
     */
    public int numberOfEntries() {
        int toReturn = 0;
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement countStatement = null;
        ResultSet rs = null;
        try {
            countStatement = dbConnection.prepareStatement(numberOfEntriesQuery());
            rs = countStatement.executeQuery();
            rs.next();
            toReturn = rs.getInt("COUNT(*)");
        } catch (SQLException e) {
            toReturn = -1;
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(countStatement);
            ConnectionFactory.close(dbConnection);
        }
        return toReturn;
    }

    //Private methods ---
    // Used in the CRUD methods

    private T retrieveObject(ResultSet resultSet) {
        T toReturn = null;
        Constructor[] constructors = type.getDeclaredConstructors();
        Constructor defaultConstructor = null;

        for(int i = 0; i < constructors.length; i++) {
            defaultConstructor = constructors[i];
            if(defaultConstructor.getGenericParameterTypes().length == 0) {
                break;
            }
        }

        try {
            resultSet.next();
            defaultConstructor.setAccessible(true);
            toReturn = (T) defaultConstructor.newInstance();
            for(Field field : type.getDeclaredFields()) {
                String fieldName = field.getName();
                Object value = resultSet.getObject(fieldName);
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                Method setter = propertyDescriptor.getWriteMethod();
                setter.invoke(toReturn, value);
            }

            } catch (Exception e) {
                return null;
        }
        return toReturn;
    }

    private Field getField(Object object, Object key) {
        Field toReturn = null;
        Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                Method getter = propertyDescriptor.getReadMethod();
                Object fieldVal = getter.invoke(object);
                if (fieldVal.equals(key)) {
                    toReturn = field;
                    break;
                }
            }
        } catch (Exception e) {
            return null;
        }

        return toReturn;
    }

    private int setFields(PreparedStatement queryStatement, Object object) throws Exception{
        Field[] fields = type.getDeclaredFields();
        int paramIndex = 1;
        for(Field field : fields) {
            if(!field.getName().equals("ID")) {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                Method getter = propertyDescriptor.getReadMethod();
                queryStatement.setObject(paramIndex, getter.invoke(object));
                paramIndex++;
            }
        }

        return paramIndex;
    }

    //Queries ---
    // Methods that return the right String to create PrepareStatement objects

    private String findQuery (String keyColumn) {
        String toReturn = "SELECT * FROM " + type.getSimpleName() + " WHERE " + keyColumn + "=?";
        return toReturn;
    }

    private String numberOfEntriesQuery() {
        String toReturn = "SELECT COUNT(*) FROM " + type.getSimpleName() + ";";
        return toReturn;
    }

    private String insertQuery() {
        String toReturn = "INSERT INTO " + type.getSimpleName() + "(";
        Field[] fields = type.getDeclaredFields();
        String valuesString = "VALUES (";
        for(Field field : fields) {
            if(!field.getName().equals("ID")) {
                toReturn += field.getName();
                valuesString += "?";
            }
            else {
                continue;
            }
            if(field != fields[fields.length - 1]) {
                toReturn += ",";
                valuesString += ",";
            }
            else {
                toReturn += ") ";
                valuesString += ");";
            }
        }
        toReturn += valuesString;
        return toReturn;
    }

    private String editQuery(Field updateKey) {
        String toReturn = "UPDATE " + type.getSimpleName() + " SET ";
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            if(field.getName().equals("ID")) {
                continue;
            }
            toReturn += field.getName() + " = ?";
            if(!field.equals(fields[fields.length - 1])) {
                toReturn += ", ";
            }
            else {
                toReturn += " WHERE " + updateKey.getName() + " = ?";
            }
        }
        return toReturn;
    }

    private String deleteQuery(Field deleteKeyField) {
        String toReturn = "DELETE FROM " + type.getSimpleName() + " WHERE ";
        toReturn += deleteKeyField.getName() + " = ?;";
        return toReturn;
    }

    private String showQuery() {
        return "SELECT * FROM " + type.getSimpleName() + ";";
    }
}
