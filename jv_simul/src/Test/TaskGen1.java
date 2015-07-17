package Test;
import Simul.TaskMng;
import Test.TEngine;

public class TaskGen1 {
	public static int idx=-1;
	public static int total=1;
	public static int gret[]={0,0,0,0,0,0,0,0,0,0};
	public int test1()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(3,1);
		tm.addTask(5,1);
		tm.addTask(7,1);
		tm.finalize();		
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
	public static void main(String[] args) throws Exception {
		Class c = TaskGen1.class;
		TaskGen1 m=new TaskGen1();
		int[] aret=TaskGen1.gret;
		int sz=TaskGen1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,1);
	}

}
