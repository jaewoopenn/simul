package vector;
import java.util.Set;
import java.util.TreeMap;

import util.SLog;


public class SupplyTM {

    private TreeMap<Integer, Integer> map;
    private TreeMap<Integer, Double> map2;

    public SupplyTM() {
        map = new TreeMap<>();
        map2 = new TreeMap<>();
    }


    // 키와 값(정수, 실수) 쌍 저장 메서드
    public void put(int key, int intValue, double doubleValue) {
        map.put(key, intValue);
        map2.put(key, doubleValue);
    }

    public int getKey(int key) {
    	if(map.containsKey(key))
    		return key;
        Integer lowerKey = map.lowerKey(key);
        if (lowerKey == null) {
            return -1;
        }
        return lowerKey;
    }
    public int getV(int key) {
    	return map.get(key);
    }
    public double getV2(int key) {
    	return map2.get(key);
    }


	public void prn() {
		for(int i:map.keySet()) {
			SLog.prn("k:"+i+" v:"+map.get(i)+", "+map2.get(i));
		}
	}


	public void reserve(int r, int d, int e) {
		int st=map.lowerKey(r);
		if(st!=r)
			put(r,0,map2.get(st));
		int et=map.lowerKey(d);
		if(et!=d)
			put(d,0,map2.get(et));
//		SLog.prn("st:"+st+","+et);
		Object[] ks=map.keySet().toArray();
		for(int i=0;i<ks.length;i++) {
			int val=(int)ks[i];
			if(val<r||val>=d)
				continue;
			int start= val;
			int end=(int)ks[i+1];
			SLog.prn("interval ["+start+","+end+"] : "+map2.get(val));
			
		}
	}
}
