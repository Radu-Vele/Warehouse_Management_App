package presentation;

import businessLogic.ClientBLL;
import model.Client;

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
            ErrorPrompt prompt = new ErrorPrompt("You actually inserted the same fields");
            return;
        }

        int status = clientBLL.editClient(toEdit, searchEmail);
        if(status != -1) {
            clientWindow.getSuccessEditLabel().setVisible(true);
        }
    }
}
