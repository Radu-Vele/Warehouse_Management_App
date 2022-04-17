package businessLogic;

import businessLogic.validators.EmailValidator;
import businessLogic.validators.PhoneNumberValidator;
import businessLogic.validators.Validator;
import dataAccess.ClientDAO;
import model.Client;

import java.util.ArrayList;

public class ClientBLL {
    //validators
    ArrayList<Validator<Client>> validators;
    ClientDAO clientDAO;

    /**
     * Initializes the validators for the client
     */
    public ClientBLL() {
        validators = new ArrayList<Validator<Client>>();
        validators.add(new EmailValidator());
        validators.add(new PhoneNumberValidator());

        clientDAO = new ClientDAO();
    }

    /**
     * Sends request to data access layer for calling the insert function.
     * @param client
     * @return
     */
    public int insertClient(Client client) {
        for(Validator v : validators) {
            String returnMessage = v.validate(client);
            if( returnMessage != "") {
                return -2;
            }
        }

        return ClientDAO.insert(client);
    }

    /**
     * @param searchEmail
     * @return Client object, or null if client not found
     */
    public Client findClientByEmail(String searchEmail) {
        return clientDAO.findByEmail(searchEmail);
    }

    /**
     * Sends a request to DAO to search for
     * @param client
     * @return
     */
    public int editClient(Client client, String searchEmail) {
        //validate changed fields
        for(Validator v : validators) {
            String returnMessage = v.validate(client);
            if(returnMessage != "") {
                return -2;
            }
        }
        return ClientDAO.edit(client, searchEmail);
    }

    public int deleteClient(String searchEmail) {
        return ClientDAO.delete(searchEmail);
    }

    public String[][] showClientTable() {
        return ClientDAO.show();
    }

    public String[] getClientEmails() {
        return ClientDAO.getEmails();
    }

    public String tableFromList(ArrayList<Object> objects) {
        return ClientDAO.tableFromList(objects);
    }
}

