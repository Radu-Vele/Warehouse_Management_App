package businessLogic.validators;

import model.Client;

import java.util.regex.Pattern;

public class PhoneNumberValidator implements Validator<Client>{
    //phone number pattern
    private static final String phone_pattern = "[0][\\d]{9}";

    @Override
    public void validate(Client t) {
        Pattern pattern = Pattern.compile(phone_pattern);
        if(!pattern.matcher(t.getPhoneNumber()).matches()){
            throw new IllegalArgumentException("The phone number is not valid"); //TODO: pop up window
        }
    }
}
