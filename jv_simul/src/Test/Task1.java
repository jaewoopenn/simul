package Test;
import java.util.Vector;

import Test.TEngine;
import Simul.Task;

public class Task1 {
	public static int idx=-1;
	public static int total=10;
	public static int gret[]={2,0,0,0,0,0,0,0,0,0};
	public int test1()
	{
		Vector<Task> tasks=new Vector<Task>();
		
		tasks.add(new Task(0,3,1));
		tasks.add(new Task(1,4,1));
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
	public static void main(String[] args) throws Exception {
		Class c = Task1.class;
		Task1 m=new Task1();
		int[] aret=Task1.gret;
		int sz=Task1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,1);
	}

}