package test;


import util.SEngineT;
import util.SLog;

public class z_array2 {
	public static void init_s() {
		s_idx=1;
//		s_idx=2;
//		s_idx=3;
		
		
		s_log_level=1;
	}

	public int test1() 
	{
		SLog.prn("hihi");
		return -1;
	}

	public int test2() {
		return -1;
	}
	public int test3() {
		return -1;
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
		z_array2.init_s();
		Class c = z_array2.class;
		z_array2 m=new z_array2();
		int[] aret=z_array2.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	private static int s_idx;
	private static int s_log_level;

}
