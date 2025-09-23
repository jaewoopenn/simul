package job;


import util.SEngineT;
import util.SLog;

public class z_sys4 {
	public static void init_s() {
		s_idx=1;
//		s_idx=2;
//		s_idx=3;
//		s_idx=4;
//		s_idx=5;
//		s_idx=6;
		
		
		s_log_level=1;
	}

	public int test1() 
	{
		JobSys js=new JobSys();
		js.add_repl(4,2,1,3);
		js.add_repl(6,1,2,2);
//		js.dbf();
		js.add_repl(5,1,0,1);
		js.prn_den();
		js.dbf();
//		js.prn_ok();
		
		return -1;
	}

	public int test2() {
		JobSys js=new JobSys();
		js.add_repl(4,2,1,3);
		js.add_repl(6,2,1,2);
//		js.dbf();
		js.add_repl(5,1,0,2);
//		js.dbf();
		js.prn_ok();
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
		z_sys4.init_s();
		Class c = z_sys4.class;
		z_sys4 m=new z_sys4();
		int[] aret=z_sys4.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	private static int s_idx;
	private static int s_log_level;

}
