package presentation;

import javax.swing.*;

public class ClientWindow extends JFrame{
    private JPanel mainPanel;
    private JTabbedPane mainTabPane;
    private JPanel deleteTab;
    private JPanel addTab;
    private JPanel editTab;
    private JPanel viewAllTab;
    private JButton insertClientButton;
    private JTextField firstNameField;
    private JLabel firstNameLabel;
    private JTextField lastNameField;
    private JLabel lastNameLabel;
    private JTextField emailField;
    private JLabel emailLabel;
    private JTextField addressField;
    private JLabel addressLabel;
    private JTextField phoneNumberField;
    private JLabel phoneNumberLabel;
    private JLabel successLabel;
    private JLabel searchEmailLabel;
    private JPanel searchPanel;
    private JTextField searchEmailField;
    private JPanel newDataPanel;
    private JTextField firstNameNewField;
    private JTextField lastNameNewField;
    private JTextField addressNewField;
    private JTextField emailNewField;
    private JCheckBox editFirstNameCheckBox;
    private JCheckBox editLastNameCheckBox;
    private JCheckBox editAddressCheckBox;
    private JCheckBox editEmailAddressCheckBox;
    private JCheckBox editPhoneNumberCheckBox;
    private JTextField phoneNumberNewField;
    private JButton editClientButton;
    private JLabel successEditLabel;
    private JTextField emailDeleteField;
    private JLabel emailDeleteLabel;
    private JLabel successDeleteLabel;
    private JButton deleteClientButton;
    private JButton showAllClientsButton;
    private JTable clientTable;
    private JButton duplicateTableButton;
    private JLabel duplicateTableLabel;
    ImageIcon imageIcon;

    ClientController clientController;

    public ClientWindow() { //TODO: Make Singleton
        setTitle("Clients Management Form");
        setSize(500, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);
        successLabel.setVisible(false);
        successEditLabel.setVisible(false);
        successDeleteLabel.setVisible(false);
        duplicateTableLabel.setVisible(false);
        imageIcon = new ImageIcon("src/main/images/customer.png");
        setIconImage(imageIcon.getImage());


        clientController = new ClientController(this);
        insertClientButton.addActionListener(clientController);
        editClientButton.addActionListener(clientController);
        deleteClientButton.addActionListener(clientController);
        showAllClientsButton.addActionListener(clientController);
        duplicateTableButton.addActionListener(clientController);
    }

    public JButton getInsertClientButton() {
        return insertClientButton;
    }

    public JLabel getFirstNameLabel() {
        return firstNameLabel;
    }

    public JLabel getSuccessLabel() {
        return successLabel;
    }

    public JTextField getEmailField() {
        return emailField;
    }

    public JTextField getFirstNameField() {
        return firstNameField;
    }

    public JTextField getLastNameField() {
        return lastNameField;
    }

    public JTextField getAddressField() {
        return addressField;
    }

    public JTextField getPhoneNumberField() {
        return phoneNumberField;
    }

    public JButton getEditClientButton() {
        return editClientButton;
    }

    public JTextField getSearchEmailField() {
        return searchEmailField;
    }

    public JCheckBox getEditFirstNameCheckBox() {
        return editFirstNameCheckBox;
    }

    public JCheckBox getEditLastNameCheckBox() {
        return editLastNameCheckBox;
    }

    public JCheckBox getEditAddressCheckBox() {
        return editAddressCheckBox;
    }

    public JCheckBox getEditEmailAddressCheckBox() {
        return editEmailAddressCheckBox;
    }

    public JCheckBox getEditPhoneNumberCheckBox() {
        return editPhoneNumberCheckBox;
    }

    public JTextField getFirstNameNewField() {
        return firstNameNewField;
    }

    public JTextField getLastNameNewField() {
        return lastNameNewField;
    }

    public JTextField getAddressNewField() {
        return addressNewField;
    }

    public JTextField getEmailNewField() {
        return emailNewField;
    }

    public JTextField getPhoneNumberNewField() {
        return phoneNumberNewField;
    }

    public JLabel getSuccessEditLabel() {
        return successEditLabel;

    }

    public JButton getDeleteClientButton() {
        return deleteClientButton;
    }

    public JTextField getEmailDeleteField() {
        return emailDeleteField;
    }

    public JLabel getSuccessDeleteLabel() {
        return successDeleteLabel;
    }

    public JButton getShowAllClientsButton() {
        return showAllClientsButton;
    }

    public JTable getClientTable() {
        return clientTable;
    }

    public JButton getDuplicateTableButton() {
        return duplicateTableButton;
    }

    public JLabel getDuplicateTableLabel() {
        return duplicateTableLabel;
    }
}
