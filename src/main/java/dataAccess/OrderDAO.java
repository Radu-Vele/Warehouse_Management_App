package dataAccess;

import connection.ConnectionFactory;
import model.Order;

import java.sql.*;
import java.util.logging.Logger;

public class OrderDAO {
    protected static final Logger LOGGER = Logger.getLogger(OrderDAO.class.getName());
    private static final String insertStatementString = "INSERT INTO order_t (clientEmail,productID,numberOfItems,processingDate)"
            + "VALUES (?,?,?,?)";

    public static int insert(Order order) {
        Connection dbConnection = ConnectionFactory.getConnection();
        int insertedID = -1; //return value
        PreparedStatement insertStatement = null;
        try {
            insertStatement = dbConnection.prepareStatement(insertStatementString, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, order.getClientEmail());
            insertStatement.setInt(2, order.getProductID());
            insertStatement.setInt(3, order.getNumberOfItems());
            insertStatement.setString(4, order.getProcessingDate());
            insertStatement.executeUpdate();

            ResultSet rs = insertStatement.getGeneratedKeys();
            if (rs.next()) {
                insertedID = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            ConnectionFactory.close(insertStatement);
            ConnectionFactory.close(dbConnection);
        }
        return insertedID;
    }
}
