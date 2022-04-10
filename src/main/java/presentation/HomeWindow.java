package presentation;

import javax.swing.*;

public class HomeWindow extends JFrame{
    private JPanel mainPanel;
    private JButton clientOperationsButton;
    private JButton productOperationsButton;
    private JButton orderOperationsButton;
    private JLabel selectLabel;

    private HomeController controller;

    public HomeWindow() {
        setTitle("Warehouse Management Application");
        setSize(500, 200);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);

        controller = new HomeController(this);
        clientOperationsButton.addActionListener(controller);
        productOperationsButton.addActionListener(controller);
        orderOperationsButton.addActionListener(controller);
    }

    public JButton getClientOperationsButton() {
        return clientOperationsButton;
    }

    public JButton getOrderOperationsButton() {
        return orderOperationsButton;
    }

    public JButton getProductOperationsButton() {
        return productOperationsButton;
    }
}