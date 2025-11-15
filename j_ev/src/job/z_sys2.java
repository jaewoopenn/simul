package job;


import util.SEngineT;
import util.SLog;

public class z_sys2 {
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
		js.add_in2(5,2);
		js.add_in2(6,2);
		js.exec(1);
		js.add_in2(3,1);
		js.exec(2);
		
		return -1;
	}

	public int test2() {
		JobSys_DEM js=new JobSys_DEM();
		js.add_in2(5,2);
		js.add_in2(16,2);
		js.add_in2(15,1);
		js.add_in2(5,2);
		js.add_in2(30,3);
		js.prn_detail();
		return -1;
	}
	public int test3() {
		JobSys_DEM js=new JobSys_DEM();
		int d=0;
		js.add_in2(4,3);
		d=js.add_in2(3,2);
		js.prn_detail();
		
		SLog.prn("---");
		js=new JobSys_DEM();
		js.add_in2(3,2);
		d=js.add_in2(4,3);
		js.prn_detail();
		
		SLog.prn("---");
		js=new JobSys_DEM();
		js.add_in2(4,3);
		d=js.add_in2(3,1);
		js.prn_detail();
		return d;
	}
	public  int test4() {
		JobSys_DEM js=new JobSys_DEM();
		js.add_in2(5,2);
		js.add(16,2,0,1);
		js.add_in2(15,1);
		js.add_in2(5,2);
		js.add_in2(30,3);
		js.prn_detail();
		js.prn_ok();
//		js.remove(1);
		SLog.prn("------");
//		js.remove_in(16,2);
		js.prn_ok();
//		js.dbf();
		return 1;
	}
	public  int test5() {
		JobSys_DEM js=new JobSys_DEM();
		int d=0;
		js=new JobSys_DEM();
		js.add_in2(5,3);
		js.add(16,2,0,1);
		js.add_in2(15,10);
		js.add_in2(30,3);
		d=js.add_in2(6,2);
		if(d>0) {
			SLog.prn("rem: "+d);
//			js.remove_in(16,2);
			d=js.add_in2(6,2);
		}
		SLog.prn("rem: "+d);
		js.prn_detail();
		return 1;
	}
	public  int test6() {
		JobSys_DEM js=new JobSys_DEM();
		js.add_in2(5,2);
		js.add(16,2,0,1);
		js.add_in2(15,1);
		js.add(5,2,0,1);
		js.add_in2(30,3);
		js.prn_detail();
		js.prn_ok();
//		js.remove(1);
		int d=js.getRem(6);
		SLog.prn("rem: "+d);
		SLog.prn("------");
		js.removeOpt(1.0,3);
		js.prn_ok();
		d=js.getRem(6);
		SLog.prn("rem: "+d);
		SLog.prn("------");
		js.removeOpt(1.0,3);
		js.prn_ok();
		d=js.getRem(6);
		SLog.prn("rem: "+d);
		js.prn_detail();
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
		z_sys2.init_s();
		Class c = z_sys2.class;
		z_sys2 m=new z_sys2();
		int[] aret=z_sys2.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	private static int s_idx;
	private static int s_log_level;

}
