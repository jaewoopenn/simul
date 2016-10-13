package testFile;
import basic.TaskMng;
import basic.TaskMngPre;
import simul.TaskSimul_EDF_AT_S;
import utilSim.TEngine;

public class TaskSetLoad1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=3;
	public static int gret[]={1,1,1,0,1,1,1,0,0,0};
	public int test1()
	{
		TaskMngPre tmp=TaskMngPre.loadFile("exp/ts/test1.txt");
		TaskMng m=tmp.freezeTasks();
		m.prn();
		return 1;
	}
	public int test2()
	{
		return 1;
	}
	public  int test3()
	{
		TaskMngPre tmp=TaskMngPre.loadFile("exp/ts/test1.txt");
		TaskMng tm=tmp.freezeTasks();
		TaskSimul_EDF_AT_S ts=new TaskSimul_EDF_AT_S(tm);
		return ts.simulEnd(0,20);
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
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
