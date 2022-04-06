package model;

public class Order {
    private Client client;
    private Product product;
    private int ID;
    private int processingDate;

    public Order(Client client, Product product, int ID, int processingDate) {
        this.client = client;
        this.product = product;
        this.ID = ID;
        this.processingDate = processingDate;
    }
}
