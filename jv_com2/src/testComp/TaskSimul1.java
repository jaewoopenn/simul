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
public class TaskSimul1 {
	public static int idx=1;
//	public static int idx=2;
//	public static int idx=3;
//	public static int idx=-1;
	public static int log_level=1;
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};

	public CompMng getComp(int set, int no) {
		String f="fc/ts/util_sim_"+(50+set*5)+"/taskset_"+no;
		SLog.prn(1, f);
		CompMng cm=CompFile.loadFile(f);
		cm.part();
		

		cm.analMaxRes();
		return cm;
	}
	// Comp
	public int test1() 
	{
		CompMng cm=getComp(5,17);
//		tm.prnComp();
		TaskMng tm=cm.getTM();
		Anal a=new AnalEDF_VD();
		a.init(tm);
		double x=a.computeX();
//		SLog.prn(1, "x:"+x);
		SysMng sm=new SysMng();
		sm.setMS_Prob(0.99);
		sm.setX(x);
//		cm.prn();
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
	public int test2() 
	{	
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
		Class c = TaskSimul1.class;
		TaskSimul1 m=new TaskSimul1();
		int[] aret=TaskSimul1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}

}
