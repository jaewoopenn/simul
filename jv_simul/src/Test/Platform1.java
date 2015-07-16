package Test;
import java.util.Vector;

import Test.TEngine;
import Simul.Task;
import Simul.Platform;

public class Platform1 {
	public static int idx=1;
	public static int total=10;
	public static int gret[]={0,0,0,0,0,0,0,0,0,0};
	public int test1()
	{
		Platform p=new Platform();
		
		Vector<Task> tasks=new Vector<Task>();
		tasks.add(new Task(0,3,1));
		tasks.add(new Task(1,4,1));
		
		p.init(tasks);
		p.simul(12);
		return 0;
	}
	public int test2()
	{
		Platform p=new Platform();
		
		Vector<Task> tasks=new Vector<Task>();
		tasks.add(new Task(0,2,1));
		tasks.add(new Task(1,3,2));
		
		p.init(tasks);
		p.simul(7);
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
