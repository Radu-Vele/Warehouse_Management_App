package presentation;

import businessLogic.ClientBLL;
import businessLogic.OrderBLL;
import businessLogic.ProductBLL;
import com.google.protobuf.Descriptors;
import model.Client;
import model.Order;
import model.Product;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Timestamp;

public class OrderController implements ActionListener {
    private  OrderWindow orderWindow;

    public OrderController(OrderWindow orderWindow) {
        this.orderWindow = orderWindow;

        //Populate the combo boxes
        ClientBLL clientBLL = new ClientBLL();
        String[] clients = clientBLL.getClientEmails();
        orderWindow.getClientComboBox().addItem(new String("<Select Client>"));
        for(String curr : clients) {
            orderWindow.getClientComboBox().addItem(curr);
        }

        ProductBLL productBLL = new ProductBLL();
        orderWindow.getProductComboBox().addItem(new String("<Select Product>"));
        String[] products = productBLL.getProductTitlesAndIDs();
        for (String curr : products) {
            orderWindow.getProductComboBox().addItem(curr);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        performOrderControl();
    }

    public void performOrderControl() {
        orderWindow.getSuccessOrderLabel().setVisible(false);

        String wantedEmail = (String)orderWindow.getClientComboBox().getSelectedItem();
        String wantedTitleAndID = (String)orderWindow.getProductComboBox().getSelectedItem();
        //check the input

        if(wantedEmail.equals("<Select Client>") || wantedTitleAndID.equals("<Select Product>") || orderWindow.getNrOfItemsField().getText().equals("")) {
            ErrorPrompt errorPrompt = new ErrorPrompt("All the fields must be completed in order to perform the order!");
            return;
        }

        try {
            int wantedID = Integer.parseInt(wantedTitleAndID.split(" : ")[0]);
            int wantedNrOfItems = Integer.parseInt(orderWindow.getNrOfItemsField().getText());
            if(wantedNrOfItems <= 0) {
                ErrorPrompt errorPrompt = new ErrorPrompt("The ordered number of items must be greater than 0!");
            }
            ProductBLL productBLL = new ProductBLL();
            ClientBLL clientBLL = new ClientBLL();

            Client foundClient = null;
            Product foundProduct = null;
            ArrayList<Object> retrievedObjects = retrieveObjects(clientBLL, productBLL, wantedEmail, wantedID, wantedNrOfItems);
            if( retrievedObjects== null) {
                return;
            }

            foundClient = (Client) retrievedObjects.get(0);
            foundProduct = (Product) retrievedObjects.get(1);

            //decrement stock
            int newStock = foundProduct.getItemsInStock() - wantedNrOfItems;
            foundProduct.setItemsInStock(newStock);

            productBLL.editProduct(foundProduct.getID(), foundProduct);

            Order newOrder = new Order(foundClient.getEmail(), foundProduct.getID(), wantedNrOfItems);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            newOrder.setProcessingDate(timestamp.toString());

            OrderBLL orderBLL = new OrderBLL();
            int orderID = orderBLL.insertOrder(newOrder);

            if(orderID == -1) {
                ErrorPrompt errorPrompt = new ErrorPrompt("Failed to insert new order to database");
            }

            newOrder.setID(orderID);

            //TODO: generate bill

            orderWindow.getSuccessOrderLabel().setVisible(true);
        } catch (NumberFormatException e) {
            ErrorPrompt errorPrompt = new ErrorPrompt("The wanted number of items must be an integer!");
            return;
        }

    }

    public ArrayList<Object> retrieveObjects(ClientBLL clientBLL, ProductBLL productBLL, String wantedEmail, int wantedID, int wantedNrOfItems) {

        ArrayList<Object> results = new ArrayList<Object>();

        Client foundClient = clientBLL.findClientByEmail(wantedEmail);
        if(foundClient == null) {
            ErrorPrompt errorPrompt = new ErrorPrompt("The client was deleted from the database in the meantime!");
            return null;
        }
        results.add(foundClient);

        Product foundProduct = productBLL.findProductByID(wantedID);
        if(foundProduct == null) {
            ErrorPrompt errorPrompt = new ErrorPrompt("The product was deleted from the database in the meantime!");
            return null;
        }
        results.add(foundProduct);

        if(foundProduct.getItemsInStock() < wantedNrOfItems) {
            ErrorPrompt errorPrompt = new ErrorPrompt("There aren't enough products in stock to perform the order!");
            return null;
        }

        return results;
    }

}
