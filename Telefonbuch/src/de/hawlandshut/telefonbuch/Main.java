package de.hawlandshut.telefonbuch;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        // write your code here

        TreeMap<String, Integer> telefonbuch = new TreeMap<>();

        telefonbuch.put("Winkler Karl", 12345);
        telefonbuch.put("Winkler Verena", 12345);
        telefonbuch.put("Bach Sebastian", 11110);

        Iterator iterator = telefonbuch.keySet().iterator();
        System.out.println("--names:--");
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        iterator = telefonbuch.values().iterator();
        System.out.println("\n--values:--");
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        System.out.println("\n--duplicates:--");
        LinkedList<Integer> valueList = new LinkedList<>(telefonbuch.values());
        valueList.sort((a, b) -> a - b);
        for (int i = 0; i < valueList.size() - 1; i++) {
            if (valueList.get(i).equals(valueList.get(i + 1))) {
                System.out.println(valueList.get(i));
            }
        }

        Set<String> keySet = telefonbuch.keySet();
        keySet.remove("Winkler Verena");
        System.out.println("\n\n" + telefonbuch.toString());

    }
}
