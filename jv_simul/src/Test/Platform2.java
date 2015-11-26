package Test;

import Util.Log;
import Util.TEngine;
import Simul.Platform;
import Simul.TaskGen;
import Simul.TaskMng;

public class Platform2 {
//	public static int idx=-1;
	public static int idx=6;
	public static int total=10;
	public static int gret[]={0,0,0,0,0, 1,-1,-1,-1,-1};

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
		return -1;
	}
	
	public  int test8()
	{
		return -1;
	}
	
	public  int test9()
	{
		return -1;
	}
	public  int test10()
	{
		return -1;
	}
	public int post(TaskMng tm,int dur){
		tm.finalize();
		double u=tm.getUtil();
		Log.prn(1, "U:"+u);
		Platform p=new Platform();
		p.init(tm);
		tm.prn();
		return p.simul(dur);
		
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
