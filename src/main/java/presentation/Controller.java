package presentation;

import businessLogic.ClientBLL;
import model.Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {
    HomeWindow homeWindow;
    public Controller(HomeWindow homeWindow) {
        this.homeWindow = homeWindow;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client newClient = new Client("Maria", "Ilea", "Str Observatorului, Nr 9", "alexandrina@email.hu", "0787253621");
        ClientBLL clientBLL = new ClientBLL();
        int id = clientBLL.insertClient(newClient);
        newClient.setID(id);
    }
}
