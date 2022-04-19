package businessLogic;

import dataAccess.ProductDAO;
import model.Product;

public class ProductBLL {
    private ProductDAO productDAO;
    public ProductBLL() {
        productDAO = new ProductDAO();
    }

    public int insertProduct(Product product) {
        if(product.getItemsInStock() < 0 ) { //number of items added must be non-negative
            return -2;
        }
        return productDAO.insert(product);
    }

    public int editProduct(int ID, Product product) {
        if(product.getItemsInStock() < 0 ) { //number of items added must be non-negative
            return -2;
        }
        return productDAO.edit(ID, product);
    }

    public Product findProductByID(int ID) {
        return productDAO.findByID(ID);
    }

    public int deleteProduct(int ID) {
        return productDAO.delete(ID);
    }

    public String[][] showProductTable() {
        return productDAO.show();
    }

    public String[] getProductTitlesAndIDs() {
        return productDAO.getTitlesAndIDs();
    }
}
