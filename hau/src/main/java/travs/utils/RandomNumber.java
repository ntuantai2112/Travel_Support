package travs.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class RandomNumber {

    public static String getRandomNumberString() throws NoSuchAlgorithmException {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = SecureRandom.getInstanceStrong();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println( RandomNumber.getRandomNumberString());
    }

}
