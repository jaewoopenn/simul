package Test;
import Test.TEngine;
import Util.Log;
import Simul.TaskMng;
public class TaskMng1 {
//	public static int idx=-1;
	public static int idx=4;
	public static int total=10;
	public static int gret[]={2,3,2,2,0,0,0,0,0,0};
	public int test1()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(3,1);
		tm.addTask(4,1);
		return tm.size();
	}
	public int test2()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(3,1);
		tm.addTask(4,1);
		tm.addTask(5,1);
		return tm.size();
	}
	public  int test3()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(3,1);
		tm.addTask(4,1);
		tm.finalize();
		int[] pt=tm.getPeriods();
		for(int p:pt)
			Log.prn(1, p);
		return pt.length;
	}
	public  int test4()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(3,1);
		tm.addTask(4,1);
		tm.finalize();
		tm.addTask(5,1);
		return tm.size();
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
		Class c = TaskMng1.class;
		TaskMng1 m=new TaskMng1();
		int[] aret=TaskMng1.gret;
		int sz=TaskMng1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,1);
	}

}