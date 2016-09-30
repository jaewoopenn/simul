package TestTaskSet;
import Basic.TaskGen;
import Basic.TaskMng;
import Exp.Platform;
import Exp.TaskSimul;
import Util.Log;
import Util.TEngine;

public class TaskSetLoad1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=2;
	public static int total=10;
	public static int gret[]={1,1,1,0,1,1,1,0,0,0};
	public int test1()
	{
		TaskGen tg=new TaskGen();
		tg.loadFile("exp/ts/test1.txt");
		TaskMng tm=new TaskMng();
		tm.setTasks(tg.getAll());
		tm.freezeTasks();
		tm.prn();
		return 1;
	}
	public int test2()
	{
		TaskGen tg=new TaskGen();
		TaskMng tm=tg.loadFileTM("exp/ts/test1.txt");
		tm.prn();
		return 1;
	}
	public  int test3()
	{
		TaskGen tg=new TaskGen();
		TaskMng tm=tg.loadFileTM("exp/ts/test1.txt");
		TaskSimul ts=new TaskSimul(tm);
		ts.init();
		ts.simulDur(0, 20);
		return ts.simulEnd(20);
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
		Class c = TaskSetLoad1.class;
		TaskSetLoad1 m=new TaskSetLoad1();
		int[] aret=TaskSetLoad1.gret;
		int sz=TaskSetLoad1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
