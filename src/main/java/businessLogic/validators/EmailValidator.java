package businessLogic.validators;

import model.Client;

import java.util.regex.Pattern;

public class EmailValidator implements Validator<Client>{
    //Email pattern
    private static final String email_pattern = "[\\w]+[@][\\w]+[\\.][\\w]{2,4}";

    @Override
    public void validate(Client t) {
        Pattern pattern = Pattern.compile(email_pattern);
        if(!pattern.matcher(t.getEmail()).matches()){
            throw new IllegalArgumentException("The email is not valid"); //TODO: pop up window
        }
    }
}
