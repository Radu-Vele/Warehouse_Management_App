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

    /**
     * Initializes the validators for the client
     */
    public ClientBLL() {
        validators = new ArrayList<Validator<Client>>();
        validators.add(new EmailValidator());
        validators.add(new PhoneNumberValidator());
    }

    /**
     * Sends request to data access layer for calling the insert function.
     * @param client
     * @return
     */
    public int insertClient(Client client) {

        for(Validator v : validators) {
            v.validate(client);
        }

        return ClientDAO.insert(client);
    }
}
