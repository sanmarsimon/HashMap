package tp2;

import java.util.Set;
import java.util.HashMap;
import java.util.Map;
//import java.util.Iterator;

public final class Interview {

    /**
     * TODO Worst Case : O(n)
     * Is valid if you flip the number upside down.
     */
    public static boolean isValidFlipped(String listOfNumbers) {
        if (listOfNumbers == "") {
            return true;
        }
        if (listOfNumbers.contains("3") || listOfNumbers.contains("4") || listOfNumbers.contains("7")) {
            return false;
        }

        StringBuilder newList = new StringBuilder(listOfNumbers);
        for (int i = 0; i < listOfNumbers.length(); i++) {
            if (listOfNumbers.charAt(i) == '6') {
                newList.setCharAt(i, '9');
            } else if (listOfNumbers.charAt(i) == '9') {
                newList.setCharAt(i, '6');
            }
        }
        newList.reverse();
        return (newList.toString().equals(listOfNumbers));

    }

    /**
     * TODO Worst Case : O(n)
     * Could be valid if you try to flip the number upside down with one of the combinations.
     */
    public static boolean isValidFlippedWithPermutation(String listOfNumbers) {

        if (listOfNumbers.length() == 1 || listOfNumbers.contains("3") || listOfNumbers.contains("4") || listOfNumbers.contains("7")) {
            return isValidFlipped(listOfNumbers);
        }

        // Map contenat pour chaque chiffre(Character) le nombres de fois qu'il se répète
        Map<Character, Integer> charOccurences = new HashMap<>();

        // On remplis la map à partir de la string listOfNumbers
        for (int i = 0; i < listOfNumbers.length(); i++) {
            if(charOccurences.containsKey(listOfNumbers.charAt(i))){
                charOccurences.replace(listOfNumbers.charAt(i),
                        charOccurences.get(listOfNumbers.charAt(i)).intValue()+1);
            }
            charOccurences.putIfAbsent(listOfNumbers.charAt(i), 1);

        }
        // Compteur qui compte le nombre de chiffres qui se répètent un nombre impair de fois
        int countOfOddOccurences = 0;

        for(Integer i : charOccurences.values()){
            if(i%2 != 0){
                countOfOddOccurences++;
            }
        }

        if (countOfOddOccurences > 1) {
            return false;
        }
        return true;
    }
}
