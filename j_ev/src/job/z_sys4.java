package job;


import util.SEngineT;
import util.SLog;
//import util.SLog;


// 필수/optional 구현

public class z_sys4 {
	public static void init_s() {
//		s_idx=1;
//		s_idx=2;
//		s_idx=3;
//		s_idx=4;
//		s_idx=5;
		s_idx=6;
		
		
		s_log_level=1;
	}

	public int test1() 
	{
		JobSys_DEM js=new JobSys_DEM();
		js.add(4,2,1,3);
		js.add(6,1,2,2);
//		js.dbf();
		js.add(5,1,0,1);
		SLog.prn("----");
		js.prn_den();
		js.prn_detail();
//		js.prn_ok();
		
		return -1;
	}

	public int test2() {
		JobSys_DEM js=new JobSys_DEM();
		js.add(4,2,1,3);
		js.add(6,1,2,2);
//		js.dbf();
		js.add(5,2,1,3);
		SLog.prn("----");
		js.prn_detail();
		js.prn_ok();
		return -1;
	}
	public int test3() {
		JobSys_DEM js=new JobSys_DEM();
		js.add(4,2,1,3);
		js.add(6,2,1,2);
		js.prn_detail();
		js.prn_ok();
		return -1;
	}
	public  int test4() {
		JobSys js=new JobSys_DEM();
		js.add(4,2,1,3);
		js.add(6,1,0,2);
		js.prn_detail();
		js.exec(6);
		js.add(4,2,1,3);
		js.add(6,1,0,2);
		js.prn_detail();
		js.prn_ok();
		return 1;
	}
	public  int test5() {
		JobSys js=new JobSys_DEM();
		js.add(2,2,0,2);
		js.add(4,1,0,1);
		js.add(6,2,1,3);
		js.add(4,1,1,3);
		js.prn_detail();
		js.prn_ok();
		
		return 1;
	}
	public  int test6() {
		JobSys js=new JobSys_DEM();
		js.add(2,2,0,2);
		js.add(4,2,0,2);
		js.add(6,0,1,1);
		js.add(6,0,1,1);
		js.add(4,1,0,2);
		js.prn_detail();
		js.prn_ok();
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
