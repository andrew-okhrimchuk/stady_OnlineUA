package CollectionAndGenerics.task1;

import java.util.*;

public class Data {
    public static List list = Arrays.asList(4, 5, -6, 4, 5, 3, 4, 2, 4, 5, 7);


    public static void main(String[] args) {
        Map<Integer, Integer> map = new TreeMap<>();

        list.stream().forEach(
                        x -> {
                            map.computeIfPresent( (Integer)x, (key, value) -> value + 1);
                            map.putIfAbsent((Integer) x, 1);
                        }
                );
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }
}
