package basic;
import basic.Task;
import basic.TaskMng;
import basic.TaskSetFile;
import util.TEngine;

public class z_TaskSetFile1 {
	public static int idx=1;
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	public static int log_level=1;
	public int test1()
	{
		TaskSetFile tmp=new TaskSetFile();
		tmp.add(new Task(3,1));
		tmp.add(new Task(4,1));
		TaskMng tm=tmp.getTM();
		tm.prn();
		TaskSetFile.writeFile("test/test.txt",tm.getTaskSet());
		return 1;
	}
	public int test2()
	{
		TaskSetFile tmp=TaskSetFile.loadFile("test/test.txt");
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
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
