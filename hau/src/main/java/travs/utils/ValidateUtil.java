package travs.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ValidateUtil {

    private static final String EMAIL_PATTERN =  "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final String IDENTIFY_CARD_PATTERN = "[0-9]{9}|[0-9]{12}";
    private static final String PWD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private static final String PHONE_NUMBER_PATTERN = "^(09|01|08|03|07|05)[0-9]{8,9}$";


    public static boolean isEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isPassword(String password) {
        Pattern pattern = Pattern.compile(PWD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    public static boolean isPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

}
