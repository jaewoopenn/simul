package job;


import util.SEngineT;
import util.SLog;

public class z_sys1 {
	public static void init_s() {
//		s_idx=1;
//		s_idx=2;
		s_idx=3;
		
		
		s_log_level=1;
	}

	public int test1() 
	{
		JobSys js=new JobSys();
		js.add(5,2);
		js.add(6,2);
		js.exec(1);
		js.add(3,1);
		js.exec(2);
		
		return -1;
	}

	public int test2() {
		JobSys js=new JobSys();
		js.add(5,2);
		js.add(16,2);
		js.add(15,1);
		js.add(5,2);
		js.add(30,3);
		js.dbf();
		return -1;
	}
	public int test3() {
		JobSys js=new JobSys();
		js.add(4,3);
		js.add(3,2);
		js.dbf();
		
		SLog.prn("---");
		js=new JobSys();
		js.add(3,2);
		js.add(4,3);
		js.dbf();
		
		SLog.prn("---");
		js=new JobSys();
		js.add(4,3);
		js.add(3,1);
		js.dbf();
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
		z_sys1.init_s();
		Class c = z_sys1.class;
		z_sys1 m=new z_sys1();
		int[] aret=z_sys1.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	private static int s_idx;
	private static int s_log_level;

}
