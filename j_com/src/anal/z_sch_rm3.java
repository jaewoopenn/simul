package anal;



import com.PRM;

import sample.TS1;
import task.TaskSet;
import util.SLog;
import util.SEngineT;

public class z_sch_rm3 {

	public static void init_s() {
//		int s=1;
		int s=2;
//		int s=3;
//		int s=4;
		
//		int log=1;
		int log=2;
		
		s_idx=s;
		s_log_level=log;
	}
	
	public int test1()
	{
//		TaskSet tm=TS1.tm1();
//		TaskSet tm=TS1.tm2();
		TaskSet tm=TS1.tm3();
		tm.sort();
		int p=3;
		AnalRM a=new AnalRM();
		a.init(tm);
		double exec=a.getExec(p);
		String st="exec:"+exec;
		tm.prnInfo(2);
		SLog.prn(2,st );
		return 0;
	}
	public int test2() 
	{
		TaskSet tm=TS1.tm3();
		tm.sort();
		PRM p=new PRM(3,1.95);
		AnalRM a=new AnalRM();
		a.init(tm,p);
		String st="";
		if(a.is_sch())
			st+=" OK";
		else
			st+=" Not OK";
		SLog.prn(2,st );
		return 0;
	}
	public  int test3()
	{
		TaskSet tm=TS1.tm3();
		tm.sort();
		int p=5;
		AnalRM a=new AnalRM();
		a.init(tm);
		double exec=a.getExec(p, 8, 133);
		String st=""+exec;
		SLog.prn(2,st );
		return 0;
	}
	public  int test4() {
		TaskSet tm=TS1.tm2();
//		TaskSet tm=TS1.tm3();
		tm.sort();
		tm.prn();
		AnalRM a=new AnalRM();
		a.init(tm);
		a.prnReq(2);
//		a.prnReq(8);
		return 1;
	}
	public  int test5() {
		TaskSet tm=TS1.tm3();
		tm.sort();
		tm.prn();
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
		z_sch_rm3.init_s();
		Class c = z_sch_rm3.class;
		z_sch_rm3 m=new z_sch_rm3();
		int[] aret=z_sch_rm3.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	private static int s_idx;
	private static int s_log_level;
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
