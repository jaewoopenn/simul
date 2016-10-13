package testTask;
import java.util.Vector;

import basic.Task;
import utill.Log;
import utill.TEngine;

public class Task1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=4;
	public static int total=10;
	public static int gret[]={2,23,0,0,0,0,0,0,0,0};
	public int test1()
	{
		Vector<Task> tasks=new Vector<Task>();
		
		tasks.add(new Task(0,3,1));
		tasks.add(new Task(1,4,1));
		return tasks.size();
	}
	public int test2() //vd
	{
		Task t=new Task(0,3,1);
		t.setVD(2.33);
		return (int)(t.vd*10);
	}
	public  int test3()
	{
		Task t=new Task(0,3,0);
		t.prn();
		Log.prn(1, " "+t.check());
		return 0;
	}
	public  int test4()
	{
		Task t=new Task(0,3,1,2);
		t.setVD(2);
		Log.prn(1," "+t.getLoUtil());
		Log.prn(1," "+t.getLoRUtil());
		Log.prn(1," "+t.getHiUtil());
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
		Class c = Task1.class;
		Task1 m=new Task1();
		int[] aret=Task1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
