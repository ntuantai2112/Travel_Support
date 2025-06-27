package travs.utils;

import org.apache.commons.lang3.RandomStringUtils;
//import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class HelperUtils {

    //private static final Logger logger = Logger.getLogger(HelperUtils.class);

    public static String formatDoubleNUmber(Double d) {
        DecimalFormat format = new DecimalFormat("0.#");
        return format.format(d);
    }

    public static String randomHotelCode() {
        return "HO-" + RandomStringUtils.random(10, true, true).toUpperCase();
    }

    public static String randomRestaurantCode() {
        return "RE-" + RandomStringUtils.random(10, true, true).toUpperCase();
    }

    public static String randomActivity() {
        return "AC-" + RandomStringUtils.random(10, true, true).toUpperCase();
    }


    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String toSlug(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    public static String genBookingID() {
        String stringID = Long.toString(System.currentTimeMillis(), 36);
        return stringID.toUpperCase();
    }

    public static String unAccent(String accentText) {
        return org.apache.commons.lang3.StringUtils.stripAccents(accentText).replace("đ", "d").replace("Đ", "D");
    }

}
