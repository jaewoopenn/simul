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
		JobSys_DEM js=new JobSys_DEM();
		int d=0;
		js.add(4,3,0,1);
		d=js.getRem(3);
		SLog.prn("rem:"+d);
		if(d<2) {
			js.removeOpt(1,1);
			d=js.getRem(3);
			SLog.prn("rem:"+d);
			
		}
		d=js.add_in(3,2);
		js.prn_dbf();
		
		return -1;
	}

	public int test2() {
		JobSys_DEM js=new JobSys_DEM();
		boolean b=js.add(4,3,0,1);
		if(b) {
			SLog.prn("OK 1");
		}
		js.prn_dbf();
		b=js.add(3,2,0,2);
		if(b) {
			SLog.prn("OK 2");
		}
		js.prn_dbf();
		b=js.add(3,2,0,1);
		if(b) {
			SLog.prn("OK 3");
		}
		js.prn_ok();
		js.prn_dbf();
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
