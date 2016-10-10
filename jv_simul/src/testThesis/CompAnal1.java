package testThesis;

import oldComp.OComp;
import oldComp.CompMng;
import anal.CompAnal;
import basic.TaskMng;
import utilSim.Log;
import utilSim.TEngine;

public class CompAnal1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=1;
	
	public static int gret[]={0,1,1,1,0,1,0,0,0,0};

	public CompMng getComp1()
	{
		CompMng cm=new CompMng();
//		TaskMng tm=new TaskMng();
//		tm.addTask(8,1);
//		tm.addHiTask(12,1,5);
//		tm.freezeTasks();
//		cm.addComp(tm);
//		tm=new TaskMng();
//		tm.addTask(8,1);
//		tm.addHiTask(12,1,5);
//		tm.freezeTasks();
//		cm.addComp(tm);
		
		return cm;
	}


	public int test1() 
	{
		CompMng cm=getComp1();
		CompAnal a=new CompAnal(cm);
		a.compute_X();
		a.comp_interface_help(0);
		return -1;
	}

	public CompMng getComp2()
	{
		CompMng cm=new CompMng();
//		TaskMng tm=new TaskMng();
//		tm.addTask(8,1);
//		tm.addHiTask(12,1,5);
//		tm.freezeTasks();
//		cm.addComp(tm);
//		tm=new TaskMng();
//		tm.addTask(9,1);
//		tm.addHiTask(12,1,5);
//		tm.freezeTasks();
//		cm.addComp(tm);
		
		return cm;
	}

	public int test2() 
	{
		CompMng cm=getComp2();
		CompAnal a=new CompAnal(cm);
		a.compute_X();
		a.set_alpha(0.4);
		a.comp_interface_help(0);
		a.comp_interface_help(1);
		return -1;
	}

	public int test3() 
	{
		CompMng cm=getComp2();
		CompAnal a=new CompAnal(cm);
		a.compute_X();
		a.set_alpha(0.4);
		double ht_lu=a.comp_interface_hi(0);
		Log.prn(1, "new ht_lu:"+ht_lu);
		ht_lu=a.comp_interface_hi(1);
		Log.prn(1, "new ht_lu:"+ht_lu);
		return -1;
	}
	
	public  int test4() 
	{
		CompMng cm=getComp2();
		CompAnal a=new CompAnal(cm);
		a.compute_X();
//		a.set_alpha(0.0);
		a.set_alpha(0.4);
//		a.set_alpha(1.0);
		TaskMng tm=a.getInterfaces();
//		tm.prn();
//		return Analysis.anal_EDF_VD(tm); 
		return 0;
	}
	public CompMng getComp3()
	{
		CompMng cm=new CompMng();
		OComp c=new OComp(0,1.0/8,1.0/12,5.0/12);
		cm.addComp(c);
		c=new OComp(0,1.0/8,1.0/12,5.0/12);
		cm.addComp(c);
		
		return cm;
	}
	
	public  int test5() 
	{
		CompMng cm=getComp3();
		CompAnal a=new CompAnal(cm);
		a.compute_X();
//		a.set_alpha(0.0);
//		a.set_alpha(0.4);
		a.set_alpha(1.0);
		TaskMng tm=a.getInterfaces();
//		return Analysis.anal_EDF_VD(tm); 
		return 0;
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
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
