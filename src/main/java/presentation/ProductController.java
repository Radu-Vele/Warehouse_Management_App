package presentation;


import businessLogic.ProductBLL;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductController implements ActionListener {
    ProductWindow productWindow;

    public ProductController(ProductWindow productWindow) {
        this.productWindow = productWindow;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if(source == productWindow.getInsertProductButton()) {
            insertProductControl();
        } else if(source == productWindow.getEditProductButton()) {
            editProductControl();
        } else if(source == productWindow.getDeleteProductButton()) {
            deleteProductControl();
        } else if(source == productWindow.getViewAllProductsButton()) {
            viewAllProductControl();
        }
    }

    public void insertProductControl() {
        productWindow.getSuccessAddLabel().setVisible(false);
        String title = productWindow.getTitleField().getText();
        String manufacturer = productWindow.getManufacturerField().getText();
        int itemsInStock;
        try {
            itemsInStock = Integer.parseInt(productWindow.getItemsInStockField().getText());
        } catch (NumberFormatException e) {
            ErrorPrompt prompt = new ErrorPrompt("The number of items must be an integer greater than 0!");
            return;
        }

        if(title.equals("") || manufacturer.equals("")) {
            ErrorPrompt prompt = new ErrorPrompt("You must complete all the text fields!");
        }

        Product newProduct = new Product(title, manufacturer, itemsInStock);

        ProductBLL productBLL = new ProductBLL();
        int id = productBLL.insertProduct(newProduct);

        if(id == -2) {
            ErrorPrompt prompt = new ErrorPrompt("The number of items must be an integer greater than 0!");
        } else if (id == -3) {
            ErrorPrompt prompt = new ErrorPrompt("An error occurred while inserting the new product");
        } else if(id != -1) {
            productWindow.getSuccessAddLabel().setVisible(true);
            newProduct.setID(id);
        }
    }

    public void editProductControl() {
        productWindow.getSuccessEditLabel().setVisible(false);
        ProductBLL productBLL = new ProductBLL();
        int ID = Integer.parseInt(productWindow.getSearchIDField().getText());
        Product foundProduct = productBLL.findProductByID(ID);
        if(foundProduct == null) {
            ErrorPrompt errorPrompt = new ErrorPrompt("There is no product with the inserted ID");
            return;
        }

        if(productWindow.getEditTitleCheckBox().isSelected()) {
            foundProduct.setTitle(productWindow.getTitleEditField().getText());
        }
        if(productWindow.getEditManufacturerCheckBox().isSelected()) {
            foundProduct.setManufacturer(productWindow.getManufacturerEditField().getText());
        }
        if(productWindow.getEditNumberOfItemsCheckBox().isSelected()) {
            try {
                int itemsInStock = Integer.parseInt(productWindow.getItemsInStockEditField().getText());
                foundProduct.setItemsInStock(itemsInStock);
            } catch (NumberFormatException e) {
                ErrorPrompt errorPrompt = new ErrorPrompt("The ID should be an integer!");
                return;
            }
        }

        int status = productBLL.editProduct(ID, foundProduct);

        if(status == -2) {
            ErrorPrompt errorPrompt = new ErrorPrompt("You must input a non-negative number of items!");
        }else if(status == -1) {
            ErrorPrompt errorPrompt = new ErrorPrompt("The edit operation was unsuccessful!");
        }else {
            productWindow.getSuccessEditLabel().setVisible(true);
        }
    }

    public void deleteProductControl() {
        productWindow.getSuccessDeleteLabel().setVisible(false);
        ProductBLL productBLL = new ProductBLL();
        try {
            int ID = Integer.parseInt(productWindow.getDeleteIDField().getText());
            int status = productBLL.deleteProduct(ID);

            if(status == -1) {
                ErrorPrompt errorPrompt = new ErrorPrompt("Product with such ID not found");
            } else {
                productWindow.getSuccessDeleteLabel().setVisible(true);
            }
        } catch (Exception e) {
            ErrorPrompt errorPrompt = new ErrorPrompt("You must input a valid (integer) ID!");
        }
    }

    public void viewAllProductControl() {
        ProductBLL productBLL = new ProductBLL();
        String[][] data = productBLL.showProductTable();

        if(data == null) {
            ErrorPrompt prompt = new ErrorPrompt("Unable to retrieve data from the table!");
        }
        String[] columnHeadings = new String[] {"Product ID", "Title", "Manufacturer", "Items in Stock"};
        DefaultTableModel tableModel = new DefaultTableModel(data, columnHeadings);
        JTable table = productWindow.getProductTable();
        table.setModel(tableModel);
    }
}