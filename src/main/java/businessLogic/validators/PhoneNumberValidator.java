package businessLogic.validators;

import model.Client;

import java.util.regex.Pattern;

public class PhoneNumberValidator implements Validator<Client>{
    //phone number pattern
    private static final String phone_pattern = "[0][\\d]{9}";

    @Override
    public String validate(Client t) {
        Pattern pattern = Pattern.compile(phone_pattern);
        if(!pattern.matcher(t.getPhoneNumber()).matches()){
            return "Error: The inserted phone number is not valid";
        }
        return "";
    }
}
