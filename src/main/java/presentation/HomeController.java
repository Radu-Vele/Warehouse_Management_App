package presentation;

import businessLogic.ClientBLL;
import model.Client;

import javax.xml.transform.Source;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

public class HomeController implements ActionListener {
    HomeWindow homeWindow;
    public HomeController(HomeWindow homeWindow) {
        this.homeWindow = homeWindow;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == homeWindow.getClientOperationsButton()) {
            ClientWindow clientWindow = new ClientWindow();
        }
        else if(source == homeWindow.getProductOperationsButton()) {
            ProductWindow productWindow = new ProductWindow();
        }
        else if(source == homeWindow.getOrderOperationsButton()) {
            OrderWindow orderWindow = new OrderWindow();
        }
    }
}
