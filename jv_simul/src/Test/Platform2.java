package Test;

import Util.Log;
import Util.TEngine;
import Simul.Platform;
import Simul.TaskGen;
import Simul.TaskMng;

public class Platform2 {
//	public static int idx=-1;
	public static int idx=10;
	public static int total=10;
	public static int gret[]={0,0,0,0,0, 1,0,1,1,1};

	public int test1()
	{
		TaskMng tm=new TaskMng();
		//-------------
		int dur=6;
		tm.addTask(5,3);
		tm.addTask(5,3);
		//-------------
		return post(tm,dur);
	}

	public int test2()
	{
		TaskMng tm=new TaskMng();
		//-------------
		int dur=12;
		tm.addTask(3,2);
		tm.addTask(4,2);
		//-------------
		return post(tm,dur);
	}

	public int test3()
	{
		TaskMng tm=new TaskMng();
		//-------------
		int dur=5;
		tm.addTask(4,3);
		tm.addTask(4,2);
		//-------------
		return post(tm,dur);
	}

	public  int test4()
	{
		TaskMng tm=new TaskMng();
		//-------------
		int dur=21;
		tm.addTask(5,3);
		tm.addTask(4,2);
		//-------------
		return post(tm,dur);
	}

	public  int test5()
	{
		TaskMng tm=new TaskMng();
		//-------------
		int dur=13;
		tm.addTask(6,1);
		tm.addTask(4,1);
		tm.addTask(3,2);
		//-------------
		return post(tm,dur);
	}
	
	public  int test6()
	{
		TaskMng tm=new TaskMng();
		//-------------
		int dur=61;
		tm.addTask(6,2);
		tm.addTask(5,2);
		tm.addTask(8,1);
		tm.addTask(15,2);
		//-------------
		return post(tm,dur);
	}
	
	public  int test7()
	{
		TaskMng tm=new TaskMng();
		//------------- // Fail
		int dur=10000;
		tm.addTask(16,5);
		tm.addTask(25,6);
		tm.addTask(38,6);
		tm.addTask(15,3);
		tm.addTask(42,3);
		tm.addTask(109,2);
		//-------------
		return post(tm,dur);
	}
	
	public  int test8()
	{
		TaskMng tm=new TaskMng();
		//------------- // Suc
		int dur=20000;
		tm.addTask(16,5);
		tm.addTask(25,6);
		tm.addTask(38,6);
		tm.addTask(15,3);
		tm.addTask(42,3);
		tm.addTask(111,2);
		//-------------
		return post(tm,dur);
	}
	
	public  int test9()
	{
		TaskMng tm=new TaskMng();
		//------------- Left Job 5 
		int dur=6;
		tm.addTask(15,4);
		tm.addTask(25,4);
		tm.addTask(35,4);
		tm.addTask(45,4);
		tm.addTask(55,4);
		tm.addTask(65,4);
		//-------------
		return post(tm,dur);
	}
	public  int test10()
	{
		TaskMng tm=new TaskMng();
		//------------- // Harmonic 1
		int dur=8193;
		tm.addTask(2,1);
		tm.addTask(4,1);
		tm.addTask(8,1);
		tm.addTask(16,1);
		tm.addTask(32,1);
		tm.addTask(64,1);
		tm.addTask(128,1);
		tm.addTask(256,2);
		//-------------
		return post(tm,dur);
	}
	public int post(TaskMng tm,int dur){
		tm.finalize();
		double u=tm.getUtil();
		Log.prn(1, "U:"+u);
		Platform p=new Platform();
		p.init(tm);
		tm.prn();
		int ret=p.simul(dur);
		Log.prn(1, "U:"+u);
		return ret;
		
	}

	public static void main(String[] args) throws Exception {
		Class c = Platform2.class;
		Platform2 m=new Platform2();
		int[] aret=Platform2.gret;
		int sz=Platform2.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,1);
	}

}
