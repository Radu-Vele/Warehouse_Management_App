package presentation;

import businessLogic.ClientBLL;
import model.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientController implements ActionListener {
    ClientWindow clientWindow;

    public ClientController(ClientWindow clientWindow) {
        this.clientWindow = clientWindow;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == clientWindow.getInsertClientButton()) {
            this.insertClientControl();
        }
        else if(source == clientWindow.getEditClientButton()) {
            this.editClientControl();
        }
        else if(source == clientWindow.getDeleteClientButton()) {
            this.deleteClientControl();
        }
        else if(source == clientWindow.getShowAllClientsButton()) {
            this.showClientTableControl();
        }
    }

    public void insertClientControl() {
        clientWindow.getSuccessLabel().setVisible(false);
        String firstName = clientWindow.getFirstNameField().getText();
        String lastName = clientWindow.getLastNameField().getText();
        String address = clientWindow.getAddressField().getText();
        String email = clientWindow.getEmailField().getText();
        String phoneNumber = clientWindow.getPhoneNumberField().getText();
        if(firstName.equals("") || lastName.equals("") || address.equals("") || email.equals("") || phoneNumber.equals("")) {
            ErrorPrompt prompt = new ErrorPrompt("You must complete all the fields!");
            return;
        }
        Client newClient = new Client(firstName, lastName, address, email, phoneNumber);
        ClientBLL clientBLL = new ClientBLL();
        int givenID = clientBLL.insertClient(newClient);
        newClient.setID(givenID);

        if(givenID == -2) { //not valid
            ErrorPrompt prompt = new ErrorPrompt("The email and/or phone number format is not valid!");
        }
        else if(givenID == -3) { //already existing email
            ErrorPrompt prompt = new ErrorPrompt("A client with the inserted email address already exists.");
        }
        else if(givenID != -1) {
            clientWindow.getSuccessLabel().setVisible(true);
        }
    }

    public void editClientControl() {
        clientWindow.getSuccessEditLabel().setVisible(false);
        String searchEmail = clientWindow.getSearchEmailField().getText();
        ClientBLL clientBLL = new ClientBLL();
        Client toEdit = clientBLL.findClientByEmail(searchEmail);
        if(toEdit == null) {
            ErrorPrompt prompt = new ErrorPrompt("There is no client having the inserted email address!");
            return;
        }
        int countChangedFields = 0;

        if(clientWindow.getEditFirstNameCheckBox().isSelected()) {
            toEdit.setFirstName(clientWindow.getFirstNameNewField().getText());
            countChangedFields++;
        }
        if(clientWindow.getEditLastNameCheckBox().isSelected()) {
            toEdit.setLastName(clientWindow.getLastNameNewField().getText());
            countChangedFields++;
        }
        if(clientWindow.getEditAddressCheckBox().isSelected()) {
            toEdit.setAddress(clientWindow.getAddressNewField().getText());
            countChangedFields++;
        }
        if(clientWindow.getEditEmailAddressCheckBox().isSelected()){
            toEdit.setEmail(clientWindow.getEmailNewField().getText());
            countChangedFields++;
        }
        if(clientWindow.getEditPhoneNumberCheckBox().isSelected()) {
            toEdit.setPhoneNumber(clientWindow.getPhoneNumberNewField().getText());
            countChangedFields++;
        }

        if(countChangedFields == 0) {
            ErrorPrompt prompt = new ErrorPrompt("You must check at least one field to update!");
            return;
        }

        int status = clientBLL.editClient(toEdit, searchEmail);
        if(status == -2) {
            ErrorPrompt prompt = new ErrorPrompt("The newly inserted email address is not valid!");
        }
        else if (status == -1) {
            ErrorPrompt prompt = new ErrorPrompt("The edit operation was unsuccessful");
        }
        else {
            clientWindow.getSuccessEditLabel().setVisible(true);
        }
    }

    public void deleteClientControl() {
        clientWindow.getSuccessDeleteLabel().setVisible(false);
        String emailDelete = clientWindow.getEmailDeleteField().getText();
        if(emailDelete.equals("")) {
            ErrorPrompt prompt = new ErrorPrompt("The email address must be inserted in the text field!");
            return;
        }
        ClientBLL clientBLL = new ClientBLL();
        int status = clientBLL.deleteClient(emailDelete);
        if(status == -1) {
            //TODO: should I find client first?
            ErrorPrompt prompt = new ErrorPrompt("There is no client having the inserted email address");
        }
        else {
            clientWindow.getSuccessDeleteLabel().setVisible(true);
        }
    }

    public void showClientTableControl() {
        ClientBLL clientBLL = new ClientBLL();
        String[][] data= clientBLL.showClientTable();
        if(data == null) {
            ErrorPrompt prompt = new ErrorPrompt("Unable to retrieve data from the table!");
        }
        String[] columnHeadings = new String[] {"ID", "First Name", "Last Name", "Address", "Email", "Phone Number"};
        DefaultTableModel tableModel = new DefaultTableModel(data, columnHeadings);
        JTable table = clientWindow.getClientTable();
        table.setModel(tableModel);
    }
}

