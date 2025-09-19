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
		JobSys js=new JobSys();
		js.add_in(5,2);
		js.add_in(6,2);
		js.exec(1);
		js.add_in(3,1);
		js.exec(2);
		
		return -1;
	}

	public int test2() {
		JobSys js=new JobSys();
		js.add_in(5,2);
		js.add_in(16,2);
		js.add_in(15,1);
		js.add_in(5,2);
		js.add_in(30,3);
		js.dbf();
		return -1;
	}
	public int test3() {
		JobSys js=new JobSys();
		int d=0;
		js.add_in(4,3);
		d=js.add_in(3,2);
		js.dbf();
		
		SLog.prn("---");
		js=new JobSys();
		js.add_in(3,2);
		d=js.add_in(4,3);
		js.dbf();
		
		SLog.prn("---");
		js=new JobSys();
		js.add_in(4,3);
		d=js.add_in(3,1);
		js.dbf();
		return d;
	}
	public  int test4() {
		JobSys js=new JobSys();
		js.add_in(5,2);
		js.add_in(16,2,1);
		js.add_in(15,1);
		js.add_in(5,2);
		js.add_in(30,3);
		js.dbf();
		js.prn_in();
//		js.remove(1);
		SLog.prn("------");
//		js.remove_in(16,2);
		js.prn_in();
//		js.dbf();
		return 1;
	}
	public  int test5() {
		JobSys js=new JobSys();
		int d=0;
		js=new JobSys();
		js.add_in(5,3);
		js.add_in(16,2,1);
		js.add_in(15,10);
		js.add_in(30,3);
		d=js.add_in(6,2);
		if(d>0) {
			SLog.prn("rem: "+d);
//			js.remove_in(16,2);
			d=js.add_in(6,2);
		}
		SLog.prn("rem: "+d);
		js.dbf();
		return 1;
	}
	public  int test6() {
		JobSys js=new JobSys();
		js.add_in(5,2);
		js.add_in(16,2,1);
		js.add_in(15,1);
		js.add_in(5,2,1);
		js.add_in(30,3);
		js.dbf();
		js.prn_in();
//		js.remove(1);
		int d=js.getRem(6);
		SLog.prn("rem: "+d);
		SLog.prn("------");
		js.removeDen(1.0);
		js.prn_in();
		d=js.getRem(6);
		SLog.prn("rem: "+d);
		SLog.prn("------");
		js.removeDen(1.0);
		js.prn_in();
		d=js.getRem(6);
		SLog.prn("rem: "+d);
		js.dbf();
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
