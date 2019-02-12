package basic;
import basic.Task;
import basic.TaskMng;
import basic.TaskSetEx;
import util.S_TEngine;

public class z_TaskSetFile1 {
	public static int idx=1;
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	public static int log_level=1;
	public int test1()
	{
		TaskSet tmp=new TaskSet();
		tmp.add(new Task(3,1));
		tmp.add(new Task(4,1));
		TaskSetEx tme=new TaskSetEx(tmp.getVec());
		TaskMng tm=tme.getTM();
		tm.prn();
		TaskSetEx.writeFile("test/test.txt",tm.getTaskSet());
		return 1;
	}
	public int test2()
	{
		TaskSetEx tmp=TaskSetEx.loadFile("test/test.txt");
		TaskMng tm=tmp.getTM();
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
		Class c = z_TaskSetFile1.class;
		z_TaskSetFile1 m=new z_TaskSetFile1();
		int[] aret=z_TaskSetFile1.gret;
		if(idx==-1)
			S_TEngine.run(m,c,aret,10);
		else
			S_TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
