package anal;



import com.PRM;

import sample.TS1;
import task.TaskSet;
import util.SLog;
import util.SEngineT;

public class z_sch_rm_dprm1 {

	public static void init_s() {
//		int s=1;
		int s=2;
//		int s=3;
//		int s=4;
		
//		int log=1;
//		int log=2;
		int log=3;
		
		s_idx=s;
		s_log_level=log;
	}
	
	public int test1()
	{
//		TaskSet tm=TS1.tm1();
//		TaskSet tm=TS1.tm2();
		TaskSet tm=TS1.tm3();
		tm.sort();
		int p=5;
		int p2=30;
		tm.prnInfo(3);
		{
			AnalRM a=new AnalRM();
			a.init(tm);
			double exec=a.getExec(p);
			String st="real exec:"+exec;
			SLog.prn(3,st +" "+exec/p );
		}
		{
			AnalRM_int a=new AnalRM_int();
			a.init(tm);
			double exec=a.getExec(p);
			String st="iPRM exec:"+exec;
			SLog.prn(3,st+" "+exec/p );
		}
		{
			AnalRM_dprm a=new AnalRM_dprm();
			a.init(tm);
			PRM r=new PRM(5,3);
			double exec=a.getExec(p2,r);
			String st="DPRM exec:"+exec;
			SLog.prn(3,st+" "+(r.getUtil()+exec/p2) );
		}
		return 0;
	}
	public int test2() 
	{
		TaskSet tm=TS1.tm3();
		tm.sort();
		int p=5;
		int p2=30;
		AnalRM_int a=new AnalRM_int();
		a.init(tm);
		double exec=a.getExec(p);
		double u1=exec/p;
		AnalRM_dprm a2=new AnalRM_dprm();
		a2.init(tm);
		PRM r=new PRM(p,exec-1);
		double exec2=a2.getExec(p2,r);
		String st="DPRM exec:"+exec2;
		double u2=r.getUtil()+exec2/p2;
		if(u1<u2)
			u2=u1;
		SLog.prn(3,st+" "+u1+" "+u2 );
		
		return 0;
	}
	public  int test3()
	{
		return 0;
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
		z_sch_rm_dprm1.init_s();
		Class c = z_sch_rm_dprm1.class;
		z_sch_rm_dprm1 m=new z_sch_rm_dprm1();
		int[] aret=z_sch_rm_dprm1.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	private static int s_idx;
	private static int s_log_level;
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
