package testComp;

import anal.AnalEDF_VD;
import comp.CompFile;
import comp.CompMng;
import comp.CompMngEx1;
import comp.TaskSimul_FC_MCS;
import comp.TaskSimul_FC_Naive;
import sim.SimulInfo;
import sim.SysMng;
import task.TaskMng;
import util.TEngine;

// Comp
public class TaskSimul5 {
	public static int idx=2;
//	public static int idx=-1;
	public static int log_level=1;
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};

	// Comp
	public int test1() 
	{
		CompMng cm=CompMngEx1.getCompMng3();
		TaskMng tm=cm.getTM();
		double x=AnalEDF_VD.computeX(tm);
		cm.setX(x);
		cm.part();
		cm.analMaxRes();
//		tm.prnComp();
		SysMng sm=new SysMng();
		sm.setMS_Prob(1.0);
		TaskSimul_FC_MCS ts=new TaskSimul_FC_MCS();
		ts.init_sm_tm(sm, tm);
		ts.set_cm(cm);
		ts.simul(0,20);
		ts.simul_end();
		return 0;
	}
	public int test2() 
	{	
		int set=8;
		int no=72;
		String f="com/ts/taskset_util_sim_"+(55+set*5)+"_"+no;
		CompMng cm=CompFile.loadFile(f);
		TaskMng tm=cm.getTM();
		double x=AnalEDF_VD.computeX(tm);
		cm.setX(x);
		cm.part();
		cm.analMaxRes();
//		tm.prnComp();
		SysMng sm=new SysMng();
		sm.setMS_Prob(0.1);
		TaskSimul_FC_MCS ts=new TaskSimul_FC_MCS();
//		TaskSimul_FC_Naive ts=new TaskSimul_FC_Naive(tm);
		ts.init_sm_tm(sm, tm);		
		ts.set_cm(cm);
		ts.isSchTab=false;
//		ts.isPrnMS=false;
		ts.isPrnEnd=false;
		ts.simul(0,20);
		ts.simul_end();
		SimulInfo si=ts.getSI();
		si.prn();
		return 0;
	}
	public int test3() 
	{
		CompMng cm=CompMngEx1.getCompMng3();
		TaskMng tm=cm.getTM();
		double x=AnalEDF_VD.computeX(tm);
		cm.setX(x);
		cm.part();
		cm.analMaxRes();
//		tm.prnComp();
		SysMng sm=new SysMng();
		sm.setMS_Prob(1.0);
		sm.setX(x);
		TaskSimul_FC_Naive ts=new TaskSimul_FC_Naive();
		ts.init_sm_tm(sm, tm);
		ts.set_cm(cm);
		ts.simul(0,20);
		ts.simul_end();
		return 0;
	}
	public  int test4() 
	{
		String f="com/ts/taskset_util_80_6";
		CompMng cm=CompFile.loadFile(f);
		TaskMng tm=cm.getTM();
		double x=AnalEDF_VD.computeX(tm);
		cm.setX(x);
		cm.part();
		cm.analMaxRes();
//		tm.prnComp();
		SysMng sm=new SysMng();
		sm.setMS_Prob(1.0);
		TaskSimul_FC_Naive ts=new TaskSimul_FC_Naive();
		ts.init_sm_tm(sm, tm);
		ts.set_cm(cm);
		ts.simul(0,100);
		ts.simul_end();		
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
		Class c = TaskSimul5.class;
		TaskSimul5 m=new TaskSimul5();
		int[] aret=TaskSimul5.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
