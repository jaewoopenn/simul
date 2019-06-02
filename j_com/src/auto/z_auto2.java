package auto;


import anal.Anal;
import anal.AnalRM;
import anal.AnalRM_iplus;
import util.SEngineT;


public class z_auto2 {
	
	public static void init_s() {
		int s=1;
//		int s=2;
//		int s=3;
//		int s=4;
		
//		int log=1;
//		int log=2;
		int log=3;
		
		s_idx=s;
		s_log_level=log;
	}
	


	public int test1()  {// config gen
		Platform p=new Platform(g_path);
		Anal a=new AnalRM();
//		Anal a=new AnalRM_iplus();
		p.anal_one3("com/t1/taskset_20.txt",  a,23);
		return 0;
	}
	public int test2() {// task set gen
		return 0;
	}
	
	public int test3() { // anal 
		return 0;
	}
	public  int test4() { // anal rs --> graph
		return 0;
 	}
	public  int test5() {
		return -1;
	}
	public  int test6() {
		return -1;
	}
	public  int test7() {
		return -1;
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
		z_auto2.init_s();
		Class c = z_auto2.class;
		z_auto2 m=new z_auto2();
		int[] aret=z_auto2.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	private static int s_idx;
	private static int s_log_level;
	private String g_path;

}
