package anal;



import com.PRM;

import sample.TS1;
import task.TaskMng;
import util.SLog;
import util.SEngineT;

public class z_sch_rm2 {
	public static int idx=1;
	public static int log_level=1;

	
	public int test1()
	{
		TaskMng tm=TS1.tm1();
//		TaskMng tm=TS1.getTM2();
		int p=3;
		int end_t=5;
		AnalRM a=new AnalRM();
		a.init(tm);
		double exec=a.getExec(p,1,end_t);
		String st="exec:"+exec;
		SLog.prn(2,st );
		return 0;
	}
	public int test2() 
	{
		TaskMng tm=TS1.tm1();
//		TaskMng tm=TS1.getTM2();
		PRM p=new PRM(3,1.5);
//		PRM p=new PRM(3,1.667);
//		PRM p=new PRM(3,2.334);
		int end_t=5;
		
		double req=MAnal.computeRBF(tm.getArr(), 1, end_t);
		double sup=p.sbf(end_t);
		String st="t:"+end_t;
		st+=" req:"+req;
		st+=" sup:"+sup;
		SLog.prn(2,st );
		return 0;
	}
	public  int test3()
	{
		TaskMng tm=TS1.tm1();
		int p=3;
		AnalRM a=new AnalRM();
		a.init(tm);
		double exec=a.getExec(p);
		String st="exec:"+exec;
		SLog.prn(2,st );
		return 0;
	}
	public  int test4()
	{
		
		return 0;
	}
	public  int test5()
	{
		return 0;
	}
	public  int test6()
	{
		return 0;
	}
	public  int test7()
	{
		return 0;
	}
	public  int test8()
	{
		return 0;
	}
	public  int test9()
	{
		return 0;
	}
	public  int test10()
	{
		return 0;
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = z_sch_rm2.class;
		z_sch_rm2 m=new z_sch_rm2();
		int[] aret=z_sch_rm2.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int total=10;
	public static int gret[]={0,0,0,0,0,0,0,0,0,0};

}
