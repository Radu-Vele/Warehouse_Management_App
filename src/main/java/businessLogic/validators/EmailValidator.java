package businessLogic.validators;

import model.Client;

import java.util.regex.Pattern;

public class EmailValidator implements Validator<Client>{
    //Email pattern
    private static final String email_pattern = "[\\w]+[@][\\w]+[.][\\w]{2,4}"; //TODO: more complex

    @Override
    public String  validate(Client t) {
        Pattern pattern = Pattern.compile(email_pattern);
        if(!pattern.matcher(t.getEmail()).matches()){
            return "Error: the inserted e-mail address is not valid";
        }
        return "";
    }
}
