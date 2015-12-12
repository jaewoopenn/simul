package TestExp;

import Util.TEngine;
import Exp.Platform;
import Simul.TaskGen;
import Simul.TaskMng;

public class Platform1 {
//	public static int idx=-1;
	public static int idx=3;
	public static int total=10;
	public static int gret[]={1,1,1,0,1,1,0,0,0,0};

	public int test1()
	{
		TaskGen tg=new TaskGen();
		String fn="test/taskset1";
		tg.loadFile(fn);
		tg.prn(1);
		TaskMng tm=new TaskMng();
		tm.setTasks(tg.getAll());
		Platform p=new Platform();
		p.init(tm);
		int ret=p.simul(20);
		return ret;
	}

	public int test2()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(2,1);
		tm.addTask(3,1);
		return post(tm,7);
	}

	public int test3()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(2,1);
		tm.addTask(5,2);
		return post(tm,10);
	}

	public  int test4()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(2,1);
		tm.addTask(3,2);
		return post(tm,7);
	}

	public  int test5()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(6,2);
		tm.addTask(9,6);
		return post(tm,18);
	}
	
	public  int test6()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(2,1);
		tm.addTask(3,1);
		tm.addTask(4,1);
		return post(tm,20);
	}
	
	public  int test7()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(10,5);
		tm.addTask(12,6);
		return post(tm,12);
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
	public int post(TaskMng tm,int dur){
		Platform p=new Platform();
		p.init(tm);
		tm.prn();
		return p.simul(dur);
		
	}

	public static void main(String[] args) throws Exception {
		Class c = Platform1.class;
		Platform1 m=new Platform1();
		int[] aret=Platform1.gret;
		int sz=Platform1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,1);
	}

}
