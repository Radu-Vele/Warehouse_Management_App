package dataAccess;

import connection.ConnectionFactory;
import model.Client;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientDAO extends GenericDAO<Client>{

    protected static final Logger LOGGER = Logger.getLogger(ClientDAO.class.getName());
    private static final String editStatementString = "UPDATE Client SET firstName = ?, lastName = ?, address = ?, email = ?, phoneNumber = ? WHERE email = ? ";
    private static final String deleteStatementString = "DELETE FROM client where email=?";
    private static final String showStatementString = "SELECT * FROM client";
    private static int nrOfTablesCreated = 0;

    public int insert(Client client) {
        return super.insert(client);
    }

    public Client findByEmail(String searchEmail) {
        return  super.find(searchEmail);
    }


    public static int edit(Client client, String searchEmail) {
        int status = 0;;
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement editStatement = null;
        try {
            editStatement = dbConnection.prepareStatement(editStatementString);
            editStatement.setString(1, client.getFirstName());
            editStatement.setString(2, client.getLastName());
            editStatement.setString(3, client.getAddress());
            editStatement.setString(4, client.getEmail());
            editStatement.setString(5, client.getPhoneNumber());
            editStatement.setString(6, searchEmail);
            editStatement.execute();

        } catch (SQLException e) {
            status = -1;
        } finally {
            ConnectionFactory.close(editStatement);
            ConnectionFactory.close(dbConnection);
        }
        return status;
    }

    public static int delete(String searchEmail) {
        int status = 0;;
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement deleteStatement = null;
        try {
            deleteStatement = dbConnection.prepareStatement(deleteStatementString);
            deleteStatement.setString(1, searchEmail);
            deleteStatement.execute();

        } catch (SQLException e) {
            status = -1;
        } finally {
            ConnectionFactory.close(deleteStatement);
            ConnectionFactory.close(dbConnection);
        }
        return status;
    }

    public static String[][] show() {
        int nrColumns = ClientDAO.numberOfEntries();
        String[][] data = new String[nrColumns][6];
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement showStatement = null;
        ResultSet rs = null;
        try {
            showStatement = dbConnection.prepareStatement(showStatementString);
            rs = showStatement.executeQuery();

            int i = 0;
            while (rs.next()) {
                data[i][0] = Integer.toString(rs.getInt("ID"));
                data[i][1] = rs.getString("firstName");
                data[i][2] = rs.getString("lastName");
                data[i][3] = rs.getString("address");
                data[i][4] = rs.getString("email");
                data[i][5] = rs.getString("phoneNumber");
                i++;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING,"Client DAO:show " + e.getMessage());
            data = null;
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(showStatement);
            ConnectionFactory.close(dbConnection);
        }
        return data;
    }

    public static int numberOfEntries() {
        int toReturn = 0;
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement countStatement = null;
        ResultSet rs = null;
        try {
            countStatement = dbConnection.prepareStatement("SELECT COUNT(*) FROM client");
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

    public static String[] getEmails() {
        String[] toReturn = new String[numberOfEntries()];
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement columnStatement = null;
        ResultSet rs = null;
        try {
            columnStatement = dbConnection.prepareStatement("SELECT email FROM client");
            rs = columnStatement.executeQuery();

            int i = 0;
            while (rs.next()) {
                toReturn[i] = rs.getString("email");
                i++;
            }

        } catch (SQLException e) {
            toReturn = null;
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(columnStatement);
            ConnectionFactory.close(dbConnection);
        }

        return toReturn;
    }

    public static String tableFromList(ArrayList<Object> list) {
        nrOfTablesCreated++;
        String[] createQueryReturned = createTableQuery(list);
        String createQuery = createQueryReturned[0];
        String returnName = createQueryReturned[1];

        //TODO: if a table with that name exists, delete it

        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement createStatement = null;

        try {
            createStatement = dbConnection.prepareStatement(createQuery);
            createStatement.execute();


            String insertQuery = insertRowQuery(list.get(0), returnName);
            for(Object object : list) {
                insertReflection(object, insertQuery);
            }

        } catch (SQLException e) {
            returnName = "-";
        } finally {
            ConnectionFactory.close(createStatement);
            ConnectionFactory.close(dbConnection);
        }
        return returnName;
    }

    public static String[] createTableQuery(ArrayList<Object> list) {
        String[] toReturn = new String[2];
        Object object = list.get(0);
        String tableName = object.getClass().getSimpleName();
        Field[] fields = object.getClass().getDeclaredFields();
        String createQuery = "CREATE TABLE" + " " + tableName + "_" + Integer.toString(nrOfTablesCreated) + " (";



        for(Field field : fields) {
            createQuery += field.getName();
            createQuery += " ";

            Type type = field.getType();
            switch (type.getTypeName()) {
                case "java.lang.String" -> createQuery += "varchar(45)";
                default -> createQuery += type.getTypeName();
            }

            if(!field.equals(fields[fields.length - 1])) {
                createQuery += ", ";
            }
        }

        createQuery += ");";
        toReturn[0] = createQuery;
        toReturn[1] = tableName + "_" + Integer.toString(nrOfTablesCreated);
        return toReturn;
    }

    public static int insertReflection(Object object, String queryStatement) {
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement insertStatement = null;
        try {
            insertStatement = dbConnection.prepareStatement(queryStatement);
            int columnIndex = 1;

            for(Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                switch (field.getType().getName()) {
                    case "int" -> insertStatement.setInt(columnIndex, (int)field.get(object));
                    case "java.lang.String" -> insertStatement.setString(columnIndex, (String) field.get(object));
                }
                columnIndex++;
            }

            insertStatement.execute();


        } catch (Exception e) {
            return -1;
        } finally {
            ConnectionFactory.close(insertStatement);
            ConnectionFactory.close(dbConnection);
        }

        return 0;
    }

    public static String insertRowQuery(Object object, String tableName) {
        String insertQuery = "INSERT INTO " + tableName + " (";

        Field[] fields = object.getClass().getDeclaredFields();

        for(int i = 0; i < fields.length; i++) {
            insertQuery += fields[i].getName();
            if(i != fields.length - 1) {
                insertQuery += ",";
            }
            else {
                insertQuery += ") ";
            }
        }

        insertQuery += "VALUES(";

        for (int i = 0; i < object.getClass().getDeclaredFields().length; i++) {
            insertQuery += '?';
            if(i != object.getClass().getDeclaredFields().length - 1) {
                insertQuery += ",";
            }
            else {
                insertQuery += ");";
            }
        }
        return insertQuery;
    }

    public static Method getGetterMethod (Object object, Field field) {
        Method[] methods = object.getClass().getMethods();
        String fieldName = field.getName();
        String desiredMethod = "get" + fieldName.substring(0, 1).toUpperCase(Locale.ROOT) + fieldName.substring(1);
        for(Method method : methods) {
            if(method.getName().equals(desiredMethod)) {
                return method;
            }
        }
        return null;
    }
}
