package vector;
import java.util.TreeMap;

class ValuePair {
    int intValue;
    double doubleValue;

    public ValuePair(int intValue, double doubleValue) {
        this.intValue = intValue;
        this.doubleValue = doubleValue;
    }

    @Override
    public String toString() {
        return "(" + intValue + ", " + doubleValue + ")";
    }
}

public class SupplyFunc {

    private TreeMap<Integer, ValuePair> map;

    public SupplyFunc() {
        map = new TreeMap<>();
    }


    // 키와 값(정수, 실수) 쌍 저장 메서드
    public void put(int key, int intValue, double doubleValue) {
        map.put(key, new ValuePair(intValue, doubleValue));
    }

    // 특정 키보다 작으면서 가장 가까운 키의 값 반환 메서드
    public int getValueOfLowerKey(int key) {
        Integer lowerKey = map.lowerKey(key);
        if (lowerKey == null) {
            return -1;
        }
        return lowerKey;
    }
    public ValuePair getV(int key) {
    	return map.get(key);
    }
    public static void main(String[] args) {
    	SupplyFunc store = new SupplyFunc();
        store.put(10, 100, 3.14);
        store.put(5, 50, 2.71);
        store.put(20, 200, 1.61);

        int queryKey = 15;
        int key=store.getValueOfLowerKey(queryKey);
        ValuePair value = store.getV(key);
        if (value != null) {
            System.out.println("키 " + queryKey + "보다 작으면서 가장 가까운 키 "+key+"의 값: " + value);
        } else {
            System.out.println("조건에 맞는 키가 없습니다.");
        }
    }
}
