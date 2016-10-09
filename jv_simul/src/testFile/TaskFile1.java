package testFile;
import basic.Task;
import basic.TaskMng;
import basic.TaskMngPre;
import utilSim.TEngine;

public class TaskFile1 {
	public static int idx=2;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	public static int log_level=1;
	public int test1()
	{
		TaskMngPre tmp=new TaskMngPre();
		tmp.add(new Task(0,3,1));
		tmp.add(new Task(0,4,1));
		TaskMng tm=tmp.freezeTasks();
		tm.prn();
		tm.writeFile("file/test.txt");
		return 1;
	}
	public int test2()
	{
		TaskMngPre tmp=new TaskMngPre();
		tmp.loadFile("file/test.txt");
		TaskMng tm=tmp.freezeTasks();
		tm.prn();
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
		Class c = TaskFile1.class;
		TaskFile1 m=new TaskFile1();
		int[] aret=TaskFile1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
