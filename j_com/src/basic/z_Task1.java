package basic;

import java.util.Vector;

import basic.Task;
import util.S_TEngine;

public class z_Task1 {
	public static int log_level=1;
	public static int idx=1;
	public static int total=10;
	public static int gret[]={2,23,0,0,0,0,0,0,0,0};
	public int test1()
	{
		Vector<Task> tasks=new Vector<Task>();
		
		tasks.add(new Task(3,1));
		tasks.add(new Task(4,1));
		return tasks.size();
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
		Class c = z_Task1.class;
		z_Task1 m=new z_Task1();
		int[] aret=z_Task1.gret;
		if(idx==-1)
			S_TEngine.run(m,c,aret,10);
		else
			S_TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
