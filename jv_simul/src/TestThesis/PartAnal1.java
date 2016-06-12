package TestThesis;

import Util.Log;
import Util.TEngine;
import Simul.Analysis;
import Simul.CompAnal;
import Simul.CompMng;
import Simul.TaskMng;

public class PartAnal1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=1;
	public static int total=10;
	public static int gret[]={0,1,1,1,0,1,0,0,0,0};

	public CompMng getComp1()
	{
		CompMng cm=new CompMng();
		TaskMng tm=new TaskMng();
		tm.addTask(8,1);
		tm.addHiTask(12,1,5);
		tm.freezeTasks();
		cm.addComp(tm);
		tm=new TaskMng();
		tm.addTask(9,1);
		tm.addHiTask(12,1,5);
		tm.freezeTasks();
		cm.addComp(tm);
		
		return cm;
	}


	public int test1() 
	{
		CompMng cm=getComp1();
		CompAnal a=new CompAnal();
		a.init(cm);
		a.compute_X();
		a.set_alpha(0.4);
		TaskMng tm=a.getInterfaces();
		tm.prn();
		return Analysis.anal_EDF_VD(tm); 
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
		Class c = PartAnal1.class;
		PartAnal1 m=new PartAnal1();
		int[] aret=PartAnal1.gret;
		int sz=PartAnal1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
