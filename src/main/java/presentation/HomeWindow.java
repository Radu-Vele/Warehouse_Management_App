package presentation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeWindow extends JFrame{
    private JPanel mainPanel;
    private JButton clientOperationsButton;
    private JButton productOperationsButton;
    private JButton orderOperationsButton;
    private JLabel selectLabel;

    private Controller controller;

    public HomeWindow() {
        setTitle("Warehouse Management Application");
        setSize(800, 300);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);

        controller = new Controller(this);
        clientOperationsButton.addActionListener(controller);
    }
}