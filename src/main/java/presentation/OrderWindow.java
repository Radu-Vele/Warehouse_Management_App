package presentation;

import javax.swing.*;

public class OrderWindow extends JFrame{
    private JPanel mainPanel;
    private JPanel selectClientPanel;
    private JPanel orderPanel;
    private JPanel selectProductPanel;
    private JButton orderButton;
    private JCheckBox iWantABillCheckBox;
    private JTextField nrOfItemsField;
    private JComboBox productComboBox;
    private JComboBox clientComboBox;
    private JLabel successOrderLabel;
    private JLabel selectClientLabel;
    private JLabel selectProductLabel;
    private OrderController controller;
    private ImageIcon imageIcon;

    public OrderWindow() {
        setTitle("Orders Management Form");
        setSize(1000, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);
        imageIcon = new ImageIcon("src/main/images/checklist.png");
        setIconImage(imageIcon.getImage());
        this.controller = new OrderController(this);
        this.successOrderLabel.setVisible(false);
        orderButton.addActionListener(controller);
    }

    public JButton getOrderButton() {
        return orderButton;
    }

    public JCheckBox getIWantABillCheckBox() {
        return iWantABillCheckBox;
    }

    public JTextField getNrOfItemsField() {
        return nrOfItemsField;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JComboBox getProductComboBox() {
        return productComboBox;
    }

    public JComboBox getClientComboBox() {
        return clientComboBox;
    }

    public JLabel getSuccessOrderLabel() {
        return successOrderLabel;
    }
}
