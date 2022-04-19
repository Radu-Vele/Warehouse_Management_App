package dataAccess;
import connection.ConnectionFactory;
import model.Product;
import java.sql.*;


public class ProductDAO extends GenericDAO<Product>{

    public int insert(Product product) {
        return super.insert(product);
    }

    public Product findByID(int ID) {
        return super.find(ID);
    }

    public int edit(int ID, Product product) {
        return super.edit(product, ID);
    }

    public int delete(int ID) {
        return super.delete(ID);
    }

    public String[][] show() {
        return super.showAll();
    }

    // Specific methods ---

    public String[] getTitlesAndIDs() {
        String[] toReturn = new String[super.numberOfEntries()];
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
