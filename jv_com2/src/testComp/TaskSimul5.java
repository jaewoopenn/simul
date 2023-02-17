package testComp;

import anal.AnalEDF_VD;
import comp.CompFile;
import comp.CompMng;
import sim.SimulInfo;
import sim.SysMng;
import sim.com.TaskSimulCom_FC;
import sim.com.TaskSimulCom_NA;
import task.TaskMng;
import util.SEngineT;
import z_ex.CompMngEx1;

// Comp
public class TaskSimul5 {
//	public static int idx=1;
//	public static int idx=2;
	public static int idx=3;
//	public static int idx=-1;
	public static int log_level=1;
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};

	// Comp
	public int test1() 
	{
		CompMng cm=CompMngEx1.getCompMng3();
		TaskMng tm=cm.getTM();
		cm.part();
		cm.analMaxRes();
//		tm.prnComp();
		SysMng sm=new SysMng();
		sm.setMS_Prob(1.0);
		sm.setX(AnalEDF_VD.computeX(tm));
		TaskSimulCom_FC ts=new TaskSimulCom_FC();
		ts.init_sm_tm(sm, tm);
		ts.set_cm(cm);
		ts.simul(0,100);
		ts.simul_end();
		SimulInfo si=ts.getSI();
		si.prn();
		return 0;
	}
	public int test2() 
	{	
		int set=8;
		int no=72;
		String f="fc/ts/util_sim_"+(55+set*5)+"/taskset_"+no;
		CompMng cm=CompFile.loadFile(f);
		TaskMng tm=cm.getTM();
		double x=AnalEDF_VD.computeX(tm);
		cm.part();
		cm.analMaxRes();
//		tm.prnComp();
		SysMng sm=new SysMng();
		sm.setMS_Prob(0.1);
		sm.setX(x);
		TaskSimulCom_FC ts=new TaskSimulCom_FC();
		ts.init_sm_tm(sm, tm);		
		ts.set_cm(cm);
		ts.simul(0,200);
		ts.simul_end();
		SimulInfo si=ts.getSI();
		si.prn();
		return 0;
	}
	public int test3() 
	{
		int set=8;
		int no=72;
		String f="fc/ts/util_sim_"+(55+set*5)+"/taskset_"+no;
		CompMng cm=CompFile.loadFile(f);
		TaskMng tm=cm.getTM();
		double x=AnalEDF_VD.computeX(tm);
		cm.part();
		cm.analMaxRes();
//		tm.prnComp();
		SysMng sm=new SysMng();
		sm.setMS_Prob(0.1);
		sm.setX(x);
		TaskSimulCom_NA ts=new TaskSimulCom_NA();
		ts.init_sm_tm(sm, tm);		
		ts.set_cm(cm);
		ts.simul(0,200);
		ts.simul_end();
		SimulInfo si=ts.getSI();
		si.prn();
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
		Class c = TaskSimul5.class;
		TaskSimul5 m=new TaskSimul5();
		int[] aret=TaskSimul5.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}

}
