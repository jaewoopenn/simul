package testUtil;
import java.util.Set;
import java.util.TreeMap;


public class z_hash2 {
    public static void main(String[] args) {
        TreeMap<Integer, Integer> map = new TreeMap<>();
        map.put(1, 100);
        map.put(2, 200);
        map.put(3, 300);
        Set<Integer> keys = map.keySet();

        // Print all keys
        for (Integer key : keys) {
            System.out.println(key);
        }
        // Get value for key 2
        int value = map.get(2); // Returns 200
        System.out.println("Value for key 2: " + value);
    }

}
