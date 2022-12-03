package ua.vspelykh.salon.util.validation;

import ua.vspelykh.salon.model.User;

public class Validation {

    private Validation() {
    }

    public static void checkEmail(String email){
         if(!email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")){
             throw new IllegalArgumentException("email is not valid");
        }
    }

    public static void checkNumber(String number){
        if (!number.matches("^\\+[0-9]{3}\\s\\((\\d+)\\)-\\d{3}-\\d{2}-\\d{2}")){
            throw new IllegalArgumentException("mobile number is not valid");
        }
    }

    public static void checkUser(User user){
        checkEmail(user.getEmail());
        checkNumber(user.getNumber());
    }
}
