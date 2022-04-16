package dataAccess;

import businessLogic.ProductBLL;
import connection.ConnectionFactory;
import model.Client;
import model.Product;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductDAO {
    protected static final Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());
    private static final String insertStatementString = "INSERT INTO product (title, manufacturer, itemsInStock)"
            + "VALUES (?, ?, ?)";
    private static final String findStatementString = "SELECT * FROM product WHERE ID =?";
    private static final String editStatementString = "UPDATE product SET title = ?, manufacturer = ?, itemsInStock = ? WHERE ID = ? ";
    private static final String deleteStatementString = "DELETE FROM product WHERE ID=?";
    private static final String showStatementString = "SELECT * FROM product";

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

    public static int delete(int ID) {
        int status = 0;
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement deleteStatement = null;
        try {
            deleteStatement = dbConnection.prepareStatement(deleteStatementString);
            deleteStatement.setInt(1, ID);
            deleteStatement.execute();

        } catch (SQLException e) {
            status = -1;
        } finally {
            ConnectionFactory.close(deleteStatement);
            ConnectionFactory.close(dbConnection);
        }
        return status;
    }

    public static int numberOfEntries() {
        int toReturn = 0;
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement countStatement = null;
        ResultSet rs = null;
        try {
            countStatement = dbConnection.prepareStatement("SELECT COUNT(*) FROM product");
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

    public static String[][] show() {
        int nrColumns = ProductDAO.numberOfEntries();
        String[][] data = new String[nrColumns][4];
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement showStatement = null;
        ResultSet rs = null;
        try {
            showStatement = dbConnection.prepareStatement(showStatementString);
            rs = showStatement.executeQuery();

            int i = 0;
            while (rs.next()) {
                data[i][0] = Integer.toString(rs.getInt("ID"));
                data[i][1] = rs.getString("title");
                data[i][2] = rs.getString("manufacturer");
                data[i][3] = Integer.toString(rs.getInt("itemsInStock"));
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

    public static String[] getTitlesAndIDs() {
        String[] toReturn = new String[numberOfEntries()];
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement columnStatement = null;
        ResultSet rs = null;
        try {
            columnStatement = dbConnection.prepareStatement("SELECT ID, title FROM product");
            rs = columnStatement.executeQuery();

            int i = 0;
            while (rs.next()) {
                toReturn[i] = Integer.toString(rs.getInt("ID"));
                toReturn[i] += " : ";
                toReturn[i] += rs.getString("title");
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
}
