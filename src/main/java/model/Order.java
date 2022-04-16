package model;

public class Order {
    private String clientEmail;
    private int productID;
    private int ID;
    private int numberOfItems;
    private String processingDate;

    public Order(String clientEmail, int productID, int numberOfItems) {
        this.clientEmail = clientEmail;
        this.productID = productID;
        this.numberOfItems = numberOfItems;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public int getProductID() {
        return productID;
    }

    public int getID() {
        return ID;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public String getProcessingDate() {
        return processingDate;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setProcessingDate(String processingDate) {
        this.processingDate = processingDate;
    }
}
