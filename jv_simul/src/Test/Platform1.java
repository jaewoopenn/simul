package Test;

import Test.TEngine;
import Simul.Platform;
import Simul.TaskMng;

public class Platform1 {
	public static int idx=-1;
//	public static int idx=5;
	public static int total=10;
	public static int gret[]={0,0,1,0,0,0,0,0,0,0};
	public int test1()
	{
		Platform p=new Platform();
		TaskMng tm=new TaskMng();
		tm.addTask(3,1);
		tm.addTask(4,1);
		
		p.init(tm);
		p.simul(12);
		return 0;
	}
	public int test2()
	{
		Platform p=new Platform();
		TaskMng tm=new TaskMng();
		tm.addTask(2,1);
		tm.addTask(3,2);
		
		p.init(tm);
		p.simul(7);
		return 0;
	}
	public  int test3()
	{
		Platform p=new Platform();
		TaskMng tm=new TaskMng();
		tm.addTask(6,2);
		tm.addTask(9,6);
		
		p.init(tm);
		return p.simul(18);
	}
	public  int test4()
	{
		Platform p=new Platform();
		TaskMng tm=new TaskMng();
		tm.addTask(6,3);
		tm.addTask(9,6);
		
		p.init(tm);
		return p.simul(18);
	}
	public  int test5()
	{
		Platform p=new Platform();
		TaskMng tm=new TaskMng();
		tm.addTask(3,2);
		tm.addTask(4,2);
		
		p.init(tm);
		return p.simul(12);
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
