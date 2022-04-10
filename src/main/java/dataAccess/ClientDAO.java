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
            LOGGER.log(Level.WARNING, "ClientDAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(insertStatement);
            ConnectionFactory.close(dbConnection);
        }
        return insertedID;
    }
}
