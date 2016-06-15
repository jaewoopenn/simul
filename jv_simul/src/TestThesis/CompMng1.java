package TestThesis;

import Util.Log;
import Util.TEngine;
import Basic.Comp;
import Basic.TaskMng;
import Simul.Analysis;
import Simul.CompAnal;
import Simul.CompMng;

public class CompMng1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=1;
	public static int total=10;
	public static int gret[]={0,1,1,1,0,1,0,0,0,0};



	public int test1() 
	{
		CompMng cm=new CompMng();
		TaskMng tm=new TaskMng();
		tm.addTask(8,1);
		tm.addHiTask(12,1,5);
		tm.freezeTasks();
		cm.addComp(tm);
		tm=new TaskMng();
		tm.addTask(8,1);
		tm.addHiTask(12,1,5);
		tm.freezeTasks();
		cm.addComp(tm);
		cm.prn();
		return -1;
	}

	public int test2() 
	{
		CompMng cm=new CompMng();
		Comp c=new Comp(0,1.0/8,1.0/12,5.0/12);
		cm.addComp(c);
		c=new Comp(0,1.0/8,1.0/12,5.0/12);
		cm.addComp(c);
		cm.prn2();
		return -1;
	}

	public int test3() 
	{
		
		Comp c=new Comp(0,1.0/8,1.0/12,5.0/12);
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
		Class c = CompMng1.class;
		CompMng1 m=new CompMng1();
		int[] aret=CompMng1.gret;
		int sz=CompMng1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
