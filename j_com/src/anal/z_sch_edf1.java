package anal;


import com.PRM;

import sample.TS1;
import task.TaskSet;
import util.SLog;
import util.SEngineT;

public class z_sch_edf1 {
//	public static int idx=1;
	public static int idx=2;
	public static int log_level=1;
//	public static int log_level=2;

	
	// check PRM 
	public int test1()
	{
		PRM p=new PRM(3,2);
//		PRM p=new PRM(3,1);
//		TaskSet tm=TS1.tm1();
		TaskSet tm=TS1.tm2();
		String st="";
		Anal a=new AnalEDF();
		a.init(tm);
		if(a.checkSch(p))
			st+="OK";
		else
			st+="Not OK";
		SLog.prn(3,st );
		return 0;
	}
	
	// getExec
	public int test2() 
	{
		TaskSet tm=TS1.tm1();
		int p=3;
		Anal a=new AnalEDF();
		a.init(tm);
		double exec=a.getExec(p);
		String st="exec:"+exec;
		SLog.prn(2,st );		
		return 0;
	}
	public  int test3()
	{
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
		Class c = z_sch_edf1.class;
		z_sch_edf1 m=new z_sch_edf1();
		int[] aret=z_sch_edf1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={0,0,0,0,0,0,0,0,0,0};

}
