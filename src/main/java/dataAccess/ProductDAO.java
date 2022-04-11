package dataAccess;

import businessLogic.ProductBLL;
import connection.ConnectionFactory;
import model.Client;
import model.Product;

import java.sql.*;
import java.util.logging.Logger;

public class ProductDAO {
    protected static final Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());
    private static final String insertStatementString = "INSERT INTO product (title, manufacturer, itemsInStock)"
            + "VALUES (?, ?, ?)";
    private static final String findStatementString = "SELECT * FROM product WHERE ID =?";
    private static final String editStatementString = "UPDATE product SET title = ?, manufacturer = ?, itemsInStock = ? WHERE ID = ? ";
    private static final String deleteStatementString = "";
    private static final String showStatementString = "";

    public static int insert(Product product) {
        Connection dbConnection = ConnectionFactory.getConnection();
        int returnID = -1;
        PreparedStatement insertStatement = null;
        try {
            insertStatement = dbConnection.prepareStatement(insertStatementString, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, product.getTitle());
            insertStatement.setString(2, product.getManufacturer());
            insertStatement.setInt(3, product.getItemsInStock());
            insertStatement.executeUpdate();

            ResultSet resultSet = insertStatement.getGeneratedKeys();
            if(resultSet.next()) {
                returnID = resultSet.getInt(1);
            }
        } catch(SQLException e) {
            return -3;
        } finally {
            ConnectionFactory.close(insertStatement);
            ConnectionFactory.close(dbConnection);
        }

        return returnID;
    }

    public static Product findByID(int ID) {
        Connection dbConnection = ConnectionFactory.getConnection();
        Product toReturn = null;
        PreparedStatement findStatement = null;
        ResultSet resultSet = null;
        try {
            findStatement = dbConnection.prepareStatement(findStatementString);
            findStatement.setInt(1, ID);
            resultSet = findStatement.executeQuery();
            resultSet.next();

            String title = resultSet.getString("title");
            String manufacturer = resultSet.getString("manufacturer");
            int itemsInStock = resultSet.getInt("itemsInStock");
            toReturn = new Product(title, manufacturer, itemsInStock);
            toReturn.setID(ID);
        } catch (SQLException e) {
            toReturn = null;
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(findStatement);
            ConnectionFactory.close(dbConnection);
        }
        return toReturn;
    }

    public static int edit(int ID, Product product) {
        int status = 0;
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement editStatement = null;
        try {
            editStatement = dbConnection.prepareStatement(editStatementString);
            editStatement.setString(1, product.getTitle());
            editStatement.setString(2, product.getManufacturer());
            editStatement.setInt(3, product.getItemsInStock());
            editStatement.setInt(4, ID);
            editStatement.execute();

        } catch (SQLException e) {
            status = -1;
        } finally {
            ConnectionFactory.close(editStatement);
            ConnectionFactory.close(dbConnection);
        }
        return status;
    }
}
