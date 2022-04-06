package model;

public class Client {
    private int ID;
    private String first_name;
    private String last_name;
    private String address;
    private String email;
    private String phoneNumber;

    public Client(int ID, String first_name, String last_name, String address, String email, String phoneNumber) {
        this.ID = ID;
        this.first_name = first_name;
        this.last_name = last_name;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    //getters and setters - when needed
    //toString overwrite
}
