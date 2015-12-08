package Test;

import Util.TEngine;
import Simul.Analysis;
import Simul.Platform;
import Simul.TaskGen;
import Simul.TaskMng;

public class Analysis1 {
//	public static int idx=-1;
	public static int idx=1;
	public static int total=10;
	public static int gret[]={0,1,1,0,1,1,0,0,0,0};

	public int test1()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(2,1);
		tm.addTask(3,1);
		Analysis a=new Analysis();
		a.init(tm);
		return 0;
	}

	public int test2()
	{
		return -1;
	}

	public int test3()
	{
		return -1;
	}

	public  int test4()
	{
		return -1;
	}

	public  int test5()
	{
		return -1;
	}
	
	public  int test6()
	{
		return -1;
	}
	
	public  int test7()
	{
		return -1;
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
		Class c = Analysis1.class;
		Analysis1 m=new Analysis1();
		int[] aret=Analysis1.gret;
		int sz=Analysis1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,1);
	}

}
