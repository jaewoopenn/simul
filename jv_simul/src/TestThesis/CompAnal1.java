package TestThesis;

import Util.Log;
import Util.TEngine;
import Simul.CompAnal;
import Simul.CompMng;
import Simul.TaskMng;

public class CompAnal1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=2;
	public static int total=10;
	public static int gret[]={0,1,1,1,0,1,0,0,0,0};

	public TaskMng getComp1()
	{
		CompMng cm=new CompMng();
		TaskMng tm=new TaskMng();
		tm.addTask(4,1);
		tm.addHiTask(6,1,5);
		tm.freezeTasks();
		cm.addComp(tm);
		
		return tm;
	}


	public int test1() 
	{
		CompMng tm=getComp1();
		CompAnal a=new CompAnal();
		a.init(tm);
		a.compute_X();
		return -1;
	}

	public int test2() 
	{
		TaskMng tm=getTask2();
		CompAnal a=new CompAnal();
		a.init(tm);
		a.set_X(0.5);
		a.help();
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
		return -1;
	}
	
	public  int test9()
	{
		return -1;
	}
	public  int test10()
	{
		return -1;
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = CompAnal1.class;
		CompAnal1 m=new CompAnal1();
		int[] aret=CompAnal1.gret;
		int sz=CompAnal1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
