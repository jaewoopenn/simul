package gen;
import util.SEngineT;

// Simulation

public class z_ComGen1 {
	public static int log_level=1;
	public static int idx=1;
	public int test1()
	{
//		ComGen t=new ComGen();
//		t.generate();
//		t.prn();
		return 1;
	}

	public int test2() {
		return 0;
	}
	public int test3() {
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
		Class c = z_ComGen1.class;
		z_ComGen1 m=new z_ComGen1();
		int[] aret=z_ComGen1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={1,1,1,1,1,1,-1,-1,-1,-1};

}
