package model;

public class Product {
    private int ID;
    private String title;
    private String manufacturer;
    private int itemsInStock;

    public Product() {

    }

    public Product(String title, String manufacturer, int itemsInStock) {
        this.title = title;
        this.manufacturer = manufacturer;
        this.itemsInStock = itemsInStock;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getItemsInStock() {
        return itemsInStock;
    }

    public void setItemsInStock(int itemsInStock) {
        this.itemsInStock = itemsInStock;
    }
}
