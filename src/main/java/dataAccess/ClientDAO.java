package dataAccess;

import connection.ConnectionFactory;
import model.Client;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientDAO {

    protected static final Logger LOGGER = Logger.getLogger(ClientDAO.class.getName());
    private static final String insertStatementString = "INSERT INTO client (firstName,lastName,address,email,phoneNumber)"
            + " VALUES (?,?,?,?,?)";
    private static final String findStatementString = "SELECT * FROM client where email=?";
    private static final String editStatementString = "UPDATE Client SET firstName = ?, lastName = ?, address = ?, email = ?, phoneNumber = ? WHERE email = ? ";
    private static final String deleteStatementString = "DELETE FROM client where email=?";
    private static final String showStatementString = "SELECT * FROM client";

    public static int insert(Client client) {
        Connection dbConnection = ConnectionFactory.getConnection();

        int insertedID = -1; //return value
        PreparedStatement insertStatement = null;
        try {
            insertStatement = dbConnection.prepareStatement(insertStatementString, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, client.getFirstName());
            insertStatement.setString(2, client.getLastName());
            insertStatement.setString(3, client.getAddress());
            insertStatement.setString(4, client.getEmail());
            insertStatement.setString(5, client.getPhoneNumber());
            insertStatement.executeUpdate();

            ResultSet rs = insertStatement.getGeneratedKeys();
            if (rs.next()) {
                insertedID = rs.getInt(1);
            }
        } catch (SQLException e) {
            //LOGGER.log(Level.WARNING, "ClientDAO:insert " + e.getMessage());
            return -3;
        } finally {
            ConnectionFactory.close(insertStatement);
            ConnectionFactory.close(dbConnection);
        }
        return insertedID;
    }

    public static Client findByEmail(String searchEmail) {
        Client toReturn = null;
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement findStatement = null;
        ResultSet rs = null;
        try {
            findStatement = dbConnection.prepareStatement(findStatementString);
            findStatement.setString(1, searchEmail);
            rs = findStatement.executeQuery();
            rs.next();

            String firstName = rs.getString("firstName");
            String lastName = rs.getString("lastName");
            String address = rs.getString("address");
            String email = rs.getString("email");
            String phoneNumber = rs.getString("phoneNumber");
            toReturn = new Client(firstName, lastName, address, email, phoneNumber);
        } catch (SQLException e) {
            //LOGGER.log(Level.WARNING,"Client DAO:findByEmail " + e.getMessage());
            toReturn = null;
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(findStatement);
            ConnectionFactory.close(dbConnection);
        }
        return toReturn;
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
            //LOGGER.log(Level.WARNING,"Client DAO:edit " + e.getMessage());
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
            //LOGGER.log(Level.WARNING,"Client DAO:edit " + e.getMessage());
            status = -1;
        } finally {
            ConnectionFactory.close(deleteStatement);
            ConnectionFactory.close(dbConnection);
        }
        return status;
    }

    public static String[][] show() {
        int nrColumns = ClientDAO.numberOfEntries();
        String[][] data = new String[nrColumns][6]; //TODO use query to get the needed number of rows;
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
            //LOGGER.log(Level.WARNING,"Client DAO:findByEmail " + e.getMessage());
            toReturn = -1;
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(countStatement);
            ConnectionFactory.close(dbConnection);
        }
        return toReturn;
    }
}
