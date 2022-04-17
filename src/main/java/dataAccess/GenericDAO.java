package dataAccess;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import connection.ConnectionFactory;
import model.Product;


public class GenericDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(GenericDAO.class.getName());
    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public GenericDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    //TODO: find

    /**
     * Searches for an entry in the database based on the key parameter
     * @return: object found of type T
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

    private String findQuery (String keyColumn) {
        String toReturn = "SELECT * FROM " + type.getSimpleName() + " WHERE " + keyColumn + "=?";
        return toReturn;
    }


    //TODO: insert
    //TODO: edit
    //TODO: delete
    //TODO: nrOfEntries
    //TODO: createAndPopulateFromList
    //TODO: getContent


}
