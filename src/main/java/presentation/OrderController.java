package presentation;

import businessLogic.ClientBLL;
import businessLogic.OrderBLL;
import businessLogic.ProductBLL;
import model.Client;
import model.OrderT;
import model.Product;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.sql.Timestamp;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

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

            OrderT newOrder = new OrderT(foundClient.getEmail(), foundProduct.getID(), wantedNrOfItems);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            newOrder.setProcessingDate(timestamp.toString());

            OrderBLL orderBLL = new OrderBLL();
            int orderID = orderBLL.insertOrder(newOrder);

            if(orderID == -1) {
                ErrorPrompt errorPrompt = new ErrorPrompt("Failed to insert new order to database");
                return;
            } else if (orderID == -3) {
                ErrorPrompt errorPrompt = new ErrorPrompt("Failed to register the order");
                return;
            }

            newOrder.setID(orderID);

            if(orderWindow.getIWantABillCheckBox().isSelected()) {
                generatePDF(newOrder, foundProduct, foundClient);
            }

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

    public void generatePDF(OrderT order, Product product, Client client) {
        //TODO: more elegant, more details
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.beginText();

            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);

            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(25, 700);

            String line1 = "The details of your order:";
            contentStream.showText(line1);
            contentStream.newLine();

            String dashed_line = "------------------------------------";
            contentStream.showText(dashed_line);
            contentStream.newLine();
            contentStream.newLine();

            String line2 = "Order ID: " + Integer.toString(order.getID());
            contentStream.showText(line2);
            contentStream.newLine();

            String line3 = "Customer: " + client.getFirstName() + " " + client.getLastName();
            contentStream.showText(line3);
            contentStream.newLine();

            String line3_1 = "Customer Email Address: " + client.getEmail();
            contentStream.showText(line3_1);
            contentStream.newLine();

            contentStream.newLine();
            contentStream.showText(dashed_line);
            contentStream.newLine();

            String line4 = "Product: \"" + product.getTitle() + "\" produced by: " + product.getManufacturer();
            contentStream.showText(line4);
            contentStream.newLine();

            String line5 = "Number of items purchased: " + Integer.toString(order.getNumberOfItems());
            contentStream.showText(line5);
            contentStream.newLine();

            contentStream.endText();
            contentStream.close();

            document.save("Receipt_" + order.getID() + "_" + order.getProcessingDate().substring(0, 10) + ".pdf");
            document.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

}
