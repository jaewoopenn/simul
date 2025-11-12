package vector;


import util.SEngineT;

public class z_test {
//	public static int idx=1;
//	public static int idx=2;
	public static int idx=3;
//	public static int idx=4;

	public static int log_level=1;

	public int test1()	{ 
    	SupplyTM store = new SupplyTM();
        store.put(0, 0, 0);
        store.put(5, 5, 0.5);
        store.put(9, 7, 0.2);
        store.put(19, 12,0);

        int queryKey = 10;
        int key=store.getKey(queryKey);
        int value = store.getV(key);
        double val2 = store.getV2(key);
        System.out.println("키 " + queryKey + "에 대한 가장 가까운 (lower) 키 "+key+"의 값: " + value+" "+val2);
		return 0;
	}

	
	public int test2() {
    	SupplyTM store = new SupplyTM();
        store.put(0, 0, 0);
        store.put(5, 5, 0.5);
        store.put(9, 7, 0.2);
        store.put(19, 12,0);
        store.prn();
		return 0;
	}
	

	public int test3() {
    	SupplyTM store = new SupplyTM();
        store.put(0, 0, 0);
        store.put(2, 2, 0.2);
        store.put(4, 3, 0);
        store.put(100, 99, 0);
        store.reserve(1,5,1);
        store.prn();
		return 0;
	}
	public  int test4() {
		return 1;
	}
	public  int test5() {
		return 1;
	}
	public  int test6() {
		return 1;
	}
	public  int test7() {
		return 1;
	}
	public  int test8() {
		return 0;
	}
	public  int test9() {
		return 0;
	}
	public  int test10() {
		return 0;
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = z_test.class;
		z_test m=new z_test();
		int[] aret=z_test.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}