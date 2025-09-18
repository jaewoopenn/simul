package job;


import util.SEngineT;
import util.SLog;

public class z_sys3 {
	public static void init_s() {
//		s_idx=1;
		s_idx=2;
//		s_idx=3;
//		s_idx=4;
//		s_idx=5;
//		s_idx=6;
		
		
		s_log_level=1;
	}

	public int test1() 
	{
		JobSys js=new JobSys();
		int d=0;
		js.add_in(4,3,1);
		d=js.getRem(3);
		SLog.prn("rem:"+d);
		if(d<2) {
			js.removeDen(1);
			d=js.getRem(3);
			SLog.prn("rem:"+d);
			
		}
		d=js.add_in(3,2);
		js.dbf();
		
		return -1;
	}

	public int test2() {
		JobSys js=new JobSys();
		boolean b=js.add_repl(4,3,1);
		if(b) {
			SLog.prn("OK 1");
		}
		js.dbf();
		b=js.add_repl(3,2,2);
		if(b) {
			SLog.prn("OK 2");
		}
		js.dbf();
		b=js.add_repl(3,2,1);
		if(b) {
			SLog.prn("OK 3");
		}
		js.prn_in();
		js.dbf();
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
		z_sys3.init_s();
		Class c = z_sys3.class;
		z_sys3 m=new z_sys3();
		int[] aret=z_sys3.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	private static int s_idx;
	private static int s_log_level;

}
