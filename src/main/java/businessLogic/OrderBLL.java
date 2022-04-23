package businessLogic;

import dataAccess.OrderDAO;
import model.Client;
import model.OrderT;
import model.Product;
import presentation.ErrorPrompt;

import java.sql.Timestamp;
import java.util.ArrayList;

public class OrderBLL {
    OrderDAO orderDAO;
    public OrderBLL() {
        this.orderDAO = new OrderDAO();
    }

    public int insertOrder(OrderT order) {
        return orderDAO.insert(order);
    }

    /**
     * Performs creates an order object based on the input received from the presentation layer
     * @param wantedEmail
     * @param wantedID
     * @param wantedNrOfItems
     * @return An array list of objects. If an ERROR is encountered, the returned list will have
     * the size 2 and contain the error message and an additional string to mark that it was an error.
     * In case of SUCCESS the returned list will contain the newly created OrderT object, the found
     * Client and the found Product
     */
    public ArrayList<Object> performOrder(String wantedEmail, int wantedID, int wantedNrOfItems) {
        ArrayList<Object> results = new ArrayList<>();
        Client foundClient = null;
        Product foundProduct = null;
        ProductBLL productBLL = new ProductBLL();

        ArrayList<Object> retrievedObjects = retrieveObjects(wantedEmail, wantedID, wantedNrOfItems);
        if(retrievedObjects.size() == 1) { //error
            results.add(retrievedObjects.get(0));
            results.add("An error was encountered");
            return results;
        }

        foundClient = (Client) retrievedObjects.get(0);
        foundProduct = (Product) retrievedObjects.get(1);

        int newStock = foundProduct.getItemsInStock() - wantedNrOfItems;
        foundProduct.setItemsInStock(newStock);

        productBLL.editProduct(foundProduct.getID(), foundProduct);

        OrderT newOrder = new OrderT(foundClient.getEmail(), foundProduct.getID(), wantedNrOfItems);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        newOrder.setProcessingDate(timestamp.toString());

        results.add(newOrder);
        results.add(foundClient);
        results.add(foundProduct);
        return results;
    }

    private ArrayList<Object> retrieveObjects(String wantedEmail, int wantedID, int wantedNrOfItems) {

        ProductBLL productBLL = new ProductBLL();
        ClientBLL clientBLL = new ClientBLL();
        ArrayList<Object> results = new ArrayList<Object>();

        Client foundClient = clientBLL.findClientByEmail(wantedEmail);
        if(foundClient == null) {
            results.add("The client was deleted from the database in the meantime!");
            return results;
        }


        Product foundProduct = productBLL.findProductByID(wantedID);
        if(foundProduct == null) {
            results.add("The product was deleted from the database in the meantime!");
            return results;
        }


        if(foundProduct.getItemsInStock() < wantedNrOfItems) {
            results.add("There aren't enough products in stock to perform the order!");
            return results;
        }

        results.add(foundClient);
        results.add(foundProduct);
        return results;
    }
}
