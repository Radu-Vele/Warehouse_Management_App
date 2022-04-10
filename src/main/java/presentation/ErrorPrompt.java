package presentation;

import javax.swing.*;
import java.awt.*;

public class ErrorPrompt extends JFrame{
    private JPanel mainPanel;
    private JLabel errorLabel;

    public ErrorPrompt(String errorMessage) {
        setTitle("ERROR");
        setSize(500, 100);
        setVisible(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);
        errorLabel.setText(errorMessage);
    }
}
