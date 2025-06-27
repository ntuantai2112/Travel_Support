package travs.utils;


import java.util.*;

public class OptionalTest {

    public static void main(String[] args) {
        isValid("({[]})");
    }

    public static boolean isValid(String input) {


        Map<String , String > map = new HashMap<>();
        map.put("(" , ")");
        map.put("{" , "}");
        map.put("[" , "]");



        Stack<String> stack = new Stack<>();

        List<String> myList = new ArrayList<String>(Arrays.asList(input.split("")));

        for (int i = 0; i < myList.size(); i++) {
            stack.push(myList.get(i));
        }



        for (int i = 0; i < stack.size(); i++) {





        }

        return false;

    }
}










