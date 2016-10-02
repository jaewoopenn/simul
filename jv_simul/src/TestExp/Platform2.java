package TestExp;

import Util.Log;
import Util.TEngine;
import Basic.Task;
import Basic.TaskMng;
import Basic.TaskMngPre;
import Exp.Platform;

public class Platform2 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=10;
	public static int gret[]={0,0,0,0,0, 1,0,1,1,1};

	public int test1()
	{
		TaskMngPre tm=new TaskMngPre();
		//-------------
		int dur=6;
		tm.add(new Task(0,5,3));
		tm.add(new Task(0,5,3));
		//-------------
		return post(tm,dur);
	}

	public int test2()
	{
		TaskMngPre tm=new TaskMngPre();
		//-------------
		int dur=12;
		tm.add(new Task(0,3,2));
		tm.add(new Task(0,4,2));
		//-------------
		return post(tm,dur);
	}

	public int test3()
	{
		TaskMngPre tm=new TaskMngPre();
		//-------------
		int dur=5;
		tm.add(new Task(0,4,3));
		tm.add(new Task(0,4,2));
		//-------------
		return post(tm,dur);
	}

	public  int test4()
	{
		TaskMngPre tm=new TaskMngPre();
		//-------------
		int dur=21;
		tm.add(new Task(0,5,3));
		tm.add(new Task(0,4,2));
		//-------------
		return post(tm,dur);
	}

	public  int test5()
	{
		TaskMngPre tm=new TaskMngPre();
		//-------------
		int dur=13;
		tm.add(new Task(0,6,1));
		tm.add(new Task(0,4,1));
		tm.add(new Task(0,3,2));
		//-------------
		return post(tm,dur);
	}
	
	public  int test6()
	{
		TaskMngPre tm=new TaskMngPre();
		//-------------
		int dur=61;
		tm.add(new Task(0,6,2));
		tm.add(new Task(0,5,2));
		tm.add(new Task(0,8,1));
		tm.add(new Task(0,15,2));
		//-------------
		return post(tm,dur);
	}
	
	public  int test7()
	{
		TaskMngPre tm=new TaskMngPre();
		//------------- // Fail
		int dur=10000;
		tm.add(new Task(0,16,5));
		tm.add(new Task(0,25,6));
		tm.add(new Task(0,38,6));
		tm.add(new Task(0,15,3));
		tm.add(new Task(0,42,3));
		tm.add(new Task(0,109,2));
		//-------------
		return post(tm,dur);
	}
	
	public  int test8()
	{
		TaskMngPre tm=new TaskMngPre();
		//------------- // Suc
		int dur=20000;
		tm.add(new Task(0,16,5));
		tm.add(new Task(0,25,6));
		tm.add(new Task(0,38,6));
		tm.add(new Task(0,15,3));
		tm.add(new Task(0,42,3));
		tm.add(new Task(0,111,2));
		//-------------
		return post(tm,dur);
	}
	
	public  int test9()
	{
		TaskMngPre tm=new TaskMngPre();
		//------------- Left Job 5 
		int dur=6;
		tm.add(new Task(0,15,4));
		tm.add(new Task(0,25,4));
		tm.add(new Task(0,35,4));
		tm.add(new Task(0,45,4));
		tm.add(new Task(0,55,4));
		tm.add(new Task(0,65,4));
		//-------------
		return post(tm,dur);
	}
	public  int test10()
	{
		TaskMngPre tm=new TaskMngPre();
		//------------- // Harmonic 1
		int dur=8193;
		tm.add(new Task(0,2,1));
		tm.add(new Task(0,4,1));
		tm.add(new Task(0,8,1));
		tm.add(new Task(0,16,1));
		tm.add(new Task(0,32,1));
		tm.add(new Task(0,64,1));
		tm.add(new Task(0,128,1));
		tm.add(new Task(0,256,2));
		//-------------
		return post(tm,dur);
	}
	public int post(TaskMngPre tmp,int dur){
		TaskMng tm=tmp.freezeTasks();
		double u=tm.getInfo().getUtil();
		Log.prn(1, "U:"+u);
		Platform p=new Platform();
		p.init(tmp);
		int ret=p.simul(dur);
		Log.prn(1, "U:"+u);
		return ret;
		
	}

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = Platform2.class;
		Platform2 m=new Platform2();
		int[] aret=Platform2.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
