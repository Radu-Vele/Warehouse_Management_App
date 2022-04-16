package presentation;

import javax.swing.*;

public class HomeWindow extends JFrame{
    private JPanel mainPanel;
    private JButton clientOperationsButton;
    private JButton productOperationsButton;
    private JButton orderOperationsButton;
    private JLabel selectLabel;
    private ImageIcon imageIcon;

    private HomeController controller;

    public HomeWindow() {
        setTitle("Warehouse Management Application");
        setSize(600, 200);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);
        imageIcon = new ImageIcon("src/main/images/open-box.png");
        setIconImage(imageIcon.getImage());
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