package dataAccess;

import connection.ConnectionFactory;
import model.Client;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;

public class ClientDAO extends GenericDAO<Client>{
    private static int nrOfTablesCreated = 0; //used to create new database tables

    public int insert(Client client) {
        return super.insert(client);
    }

    public Client findByEmail(String searchEmail) {
        return  super.find(searchEmail);
    }

    public int edit(Client client, String searchEmail) {
        return super.edit(client, searchEmail);
    }

    public int delete(String searchEmail) {
        return  super.delete(searchEmail);
    }

    public String[][] show() {
        return super.showAll();
    }


    public String[] getEmails() {
        String[] toReturn = new String[super.numberOfEntries()];
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
}
