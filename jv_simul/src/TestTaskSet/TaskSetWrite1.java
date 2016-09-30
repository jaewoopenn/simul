package TestTaskSet;
import Basic.TaskGen;
import Basic.TaskMng;
import Exp.Platform;
import Util.Log;
import Util.TEngine;

public class TaskSetWrite1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=2;
	public static int total=10;
	public static int gret[]={1,1,1,0,1,1,1,0,0,0};
	public int test1()
	{
		TaskGen tg=new TaskGen();
		tg.setUtil(0.5,0.8);
		tg.setPeriod(20,50);
		tg.setTUtil(0.001,0.1);
		tg.generate();
		tg.prn(1);
		return tg.check();
	}
	public int test2()
	{
		TaskGen tg=new TaskGen();
		tg.setUtil(0.45,0.5);
		tg.setPeriod(20,50);
		tg.setTUtil(0.01,0.1);
		tg.generate();
		tg.writeFile("exp/ts/test1.txt");
		return 1;
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
		Class c = TaskSetWrite1.class;
		TaskSetWrite1 m=new TaskSetWrite1();
		int[] aret=TaskSetWrite1.gret;
		int sz=TaskSetWrite1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
