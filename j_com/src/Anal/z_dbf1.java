package Anal;


import basic.Task;
import basic.TaskMng;
import basic.TaskSet;
import util.S_Log;
import util.S_TEngine;

public class z_dbf1 {
	public static int log_level=1;
	public static int idx=1;
	public static int total=10;
	public static int gret[]={2,23,0,0,0,0,0,0,0,0};
	public int test1()
	{
		TaskSet ts=new TaskSet();
		ts.add(new Task(3,1));
		ts.add(new Task(4,1));
		ts.end();
		TaskMng tm=new TaskMng(ts);
		
		for(int t=0;t<12;t++) {
			double r=AUtil_RM.computeRBF(tm.getArr(),2,t);
			S_Log.prnc(1, "t:"+t);
			S_Log.prn(1, " r:"+r);
		}
		return 0;
	}
	public int test2() 
	{
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
		Class c = z_dbf1.class;
		z_dbf1 m=new z_dbf1();
		int[] aret=z_dbf1.gret;
		if(idx==-1)
			S_TEngine.run(m,c,aret,10);
		else
			S_TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
