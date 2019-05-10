package Anal;


import com.PRM;

import basic.Task;
import basic.TaskMng;
import basic.TaskSet;
import util.S_Log;
import util.S_TEngine;

public class z_sch1 {
	public static int idx=4;
	public static int log_level=1;

	public TaskMng getTM1() {
		TaskSet ts=new TaskSet();
		ts.add(new Task(4,1));
		ts.add(new Task(6,1));
		ts.end();
		return new TaskMng(ts);
	}
	
	public int test1()
	{
		PRM p=new PRM(3,1.5);
		TaskMng tm=getTM1();
		
		S_Log.prn(1, "t \t sup \t req ");
		for(int t=0;t<12;t++) {
			double s=p.sbf(t);
			double r=AUtil_RM.computeRBF(tm.getArr(),1,t);
			String st=t+"\t"+s+"\t"+r+"\t";
			if (s>r)
				st+=">>>>>";
			else
				st+="<";
			S_Log.prn(1,st );
		}
		return 0;
	}
	public int test2() 
	{
		PRM p=new PRM(3,1.5);
//		PRM p=new PRM(3,1);
		TaskMng tm=getTM1();
		String st="";
		if(AUtil_RM.checkSch_ind(p,tm,1,13))
			st+="OK";
		else
			st+="Not OK";
		S_Log.prn(2,st );
			
		return 0;
	}
	public  int test3()
	{
		PRM p=new PRM(3,2);
		TaskMng tm=getTM1();
		String st="";
		if(AUtil_RM.checkSch(p,tm))
			st+="OK";
		else
			st+="Not OK";
		S_Log.prn(2,st );
		return 0;
	}
	public  int test4()
	{
		TaskMng tm=getTM1();
		int p=3;
		double exec=AUtil_RM.getExec(tm,p);
		String st="exec:"+exec;
		S_Log.prn(2,st );
		
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
		Class c = z_sch1.class;
		z_sch1 m=new z_sch1();
		int[] aret=z_sch1.gret;
		if(idx==-1)
			S_TEngine.run(m,c,aret,10);
		else
			S_TEngine.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int total=10;
	public static int gret[]={0,0,0,0,0,0,0,0,0,0};

}
