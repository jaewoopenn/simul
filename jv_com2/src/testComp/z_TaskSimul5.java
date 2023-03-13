package testComp;

import anal.Anal;
import anal.AnalEDF_AD_E;
import anal.AnalEDF_VD;
import comp.AnalComp;
import comp.CompFile;
import comp.CompMng;
import sim.SimulInfo;
import sim.SysMng;
import sim.com.TaskSimulCom_FC;
import sim.com.TaskSimulCom_NA;
import task.TaskMng;
import util.SEngineT;
import util.SLog;
import z_ex.CompMngEx1;

// Comp
public class z_TaskSimul5 {
//	public static int idx=1;
	public static int idx=2;
//	public static int idx=3;
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
//		int set=8;
		int set=7;
		int no=0;
		String f="fc/ts/util_sim_"+(50+set*5)+"/taskset_"+no;
		SLog.prn(1, f);
		CompMng cm=CompFile.loadFile(f);
		cm.part();
		AnalComp ac=new AnalComp(cm);
		ac.computeX(0);
//		int det=ac.anal(0);
		int det=ac.anal(1);
		if(det==0)
			return 0;
		
		TaskMng tm=cm.getTM();
		Anal a=new AnalEDF_AD_E();
		a.init(tm);
		double x=a.computeX();

		cm.part();
		cm.analMaxRes();
//		tm.prnComp();
		SysMng sm=new SysMng();
		sm.setMS_Prob(0.99);
		sm.setX(x);
		TaskSimulCom_FC ts=new TaskSimulCom_FC();
//		TaskSimulCom_NA ts=new TaskSimulCom_NA();
		ts.init_sm_tm(sm, tm);		
		ts.set_cm(cm);
		ts.simul(0,10000);
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
		Class c = z_TaskSimul5.class;
		z_TaskSimul5 m=new z_TaskSimul5();
		int[] aret=z_TaskSimul5.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}

}
