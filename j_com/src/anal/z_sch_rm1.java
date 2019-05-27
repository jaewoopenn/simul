package anal;


import com.PRM;

import sample.TS1;
import task.TaskSet;
import util.SLog;
import util.SEngineT;

public class z_sch_rm1 {
	public static int idx=4;
	public static int log_level=2;

	
	public int test1()
	{
		PRM p=new PRM(3,1.5);
		TaskSet tm=TS1.tm1();
		
		SLog.prn(1, "t \t sup \t req ");
		for(int t=0;t<12;t++) {
			double s=p.sbf(t);
			double r=tm.computeRBF(1,t);
			String st=t+"\t"+s+"\t"+r+"\t";
			if (s>r)
				st+=">>>>>";
			else
				st+="<";
			SLog.prn(1,st );
		}
		return 0;
	}
	public int test2() 
	{
		PRM p=new PRM(3,1.5);
//		PRM p=new PRM(3,1);
		TaskSet tm=TS1.tm1();
		String st="";
		AnalRM a=new AnalRM();
		a.init(tm);
		if(a.checkSch_ind(p,1,13))
			st+="OK";
		else
			st+="Not OK";
		SLog.prn(2,st );
			
		return 0;
	}
	public  int test3()
	{
		PRM p=new PRM(3,2);
		TaskSet tm=TS1.tm1();
		String st="";
		AnalRM a=new AnalRM();
		a.init(tm);
		if(a.checkSch(p))
			st+="OK";
		else
			st+="Not OK";
		SLog.prn(3,st );
		return 0;
	}
	public  int test4()
	{
//		TaskSet tm=TS1.tm1();
		TaskSet tm=TS1.tm3();
		int p=3;
		AnalRM a=new AnalRM();
		a.init(tm);
		double exec=a.getExec(p);
		String st="exec:"+exec;
		SLog.prn(2,st );
		tm.prnInfo(2);
		
		return 0;
	}
	public  int test5()
	{
		TaskSet tm=TS1.tm1();
		PRM p=new PRM(3,1.6667);
		String st="";
		AnalRM a=new AnalRM();
		a.init(tm);
		a.setPRM(p);
		if(a.is_sch())
			st+="OK";
		else
			st+="Not OK";
		SLog.prn(2,st );
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
		Class c = z_sch_rm1.class;
		z_sch_rm1 m=new z_sch_rm1();
		int[] aret=z_sch_rm1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={0,0,0,0,0,0,0,0,0,0};

}
