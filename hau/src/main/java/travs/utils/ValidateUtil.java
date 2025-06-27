package travs.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ValidateUtil {

    private static final String PHONE_NUMBER_PATTERN = "^0[0-9]{9}";
    private static final String EMAILPATTERN = "^(.+)@(\\S+)$";
    private static final String IDENTIFY_CARD_PATTERN = "[0-9]{9}|[0-9]{12}";
    private static final String PWDPATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    public static boolean isEmail(String email) {
        Pattern pattern = Pattern.compile(EMAILPATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isPassword(String password) {
        Pattern pattern = Pattern.compile(PWDPATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    public static boolean isPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN1);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
    private static final String PHONE_NUMBER_PATTERN1 = "^(09|01|08|03|07|05)[0-9]{8,9}$";

}
