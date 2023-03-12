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
public class z_TaskSimul1 {
	public static int idx=1;
//	public static int idx=2;
//	public static int idx=3;
//	public static int idx=-1;
	public static int log_level=1;
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};

	public CompMng getComp(int set, int no) {
		String f="fc/ts/util_sim_"+(60+set*4)+"/taskset_"+no;
		SLog.prn(1, f);
		CompMng cm=CompFile.loadFile(f);
		cm.part();
		double x=AnalEDF_VD.computeX(cm.getTM());
		cm.setX(x);

		cm.analMaxRes();
//		cm.prn();
		return cm;
	}
	// Comp
	public int test1() 
	{
		CompMng cm=getComp(6,87);
		TaskMng tm=cm.getTM();
		double dtm=AnalEDF_VD.dtm(tm);
		SLog.prn(1,"dtm:"+dtm);
//		double x=AnalEDF_VD.computeX(tm);
//		SysMng sm=new SysMng();
//		sm.setMS_Prob(0.4);
//		sm.setX(x);
//		cm.prn();
//		TaskSimulCom_FC ts=new TaskSimulCom_FC();
////		TaskSimulCom_NA ts=new TaskSimulCom_NA();
//		ts.init_sm_tm(sm, tm);		
//		ts.set_cm(cm);
//		ts.simul(0,10000);
//		ts.simul_end();
//		SimulInfo si=ts.getSI();
//		SLog.prn(1, ts.getName());
//		si.prn();
		return 0;
	}
	public int test2() 
	{	
		CompMng cm=getComp(6,87);
//		tm.prnComp();
		TaskMng tm=cm.getTM();
		double x=AnalEDF_VD.computeX(tm);
		SysMng sm=new SysMng();
		sm.setMS_Prob(0.55);
		sm.setX(x);
//		cm.prn();
//		TaskSimulCom_FC ts=new TaskSimulCom_FC();
		TaskSimulCom_NA ts=new TaskSimulCom_NA();
		ts.init_sm_tm(sm, tm);		
		ts.set_cm(cm);
		ts.simul(0,10000);
		ts.simul_end();
		SimulInfo si=ts.getSI();
		SLog.prn(1, ts.getName());
		si.prn();
		return 0;

	}
	public int test3() 
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
		Class c = z_TaskSimul1.class;
		z_TaskSimul1 m=new z_TaskSimul1();
		int[] aret=z_TaskSimul1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}

}
