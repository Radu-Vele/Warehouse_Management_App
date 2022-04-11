package businessLogic;

import dataAccess.ProductDAO;
import model.Product;

public class ProductBLL {
    public ProductBLL() {
    }

    public int insertProduct(Product product) {
        if(product.getItemsInStock() < 0 ) { //number of items added must be non-negative
            return -2;
        }
        return ProductDAO.insert(product);
    }

    public int editProduct(int ID, Product product) {
        if(product.getItemsInStock() < 0 ) { //number of items added must be non-negative
            return -2;
        }
        return ProductDAO.edit(ID, product);
    }

    public Product findProductByID(int ID) {
        return ProductDAO.findByID(ID);
    }
}
