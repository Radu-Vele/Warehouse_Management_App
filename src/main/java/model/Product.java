package model;

public class Product {
    private int ID;
    private String title;
    private String manufacturer;
    private int itemsInStock;

    public Product(int ID, String title, String manufacturer) {
        this.ID = ID;
        this.title = title;
        this.manufacturer = manufacturer;
        this.itemsInStock = 0;
    }
}
