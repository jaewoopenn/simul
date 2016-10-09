package testFile;
import comp.CompMng;

import basic.Task;
import basic.TaskMng;
import basic.TaskMngPre;
import taskSetEx.CompEx1;
import utilSim.TEngine;

public class CompFile1 {
	public static int idx=1;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	public static int log_level=1;
	public int test1()
	{
		CompMng cm=CompEx1.getComp1();
		cm.prn();
		cm.writeFile("file/testCom.txt");
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
		Class c = CompFile1.class;
		CompFile1 m=new CompFile1();
		int[] aret=CompFile1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
