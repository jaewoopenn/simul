package sim.mc;

import anal.Anal;
import anal.AnalEDF_AD_E;
import anal.AnalEDF_VD;
import comp.CompMng;
import sim.SimulInfo;
import sim.SysMng;
import sim.com.TaskSimulCom_NA;
import task.TaskMng;
import util.SEngineT;
import util.SLogF;
import z_ex.CompMngEx1;
import z_ex.TS_MC3;

public class z_TaskSimul4 {
//	public static int idx=1;
//	public static int idx=2;
	public static int idx=3;
	public static int log_level=1;


	public SysMng getSM() {
		SysMng sm=new SysMng();
//		sm.setMS_Prob(0.0);
		sm.setMS_Prob(0.5);
//		sm.setMS_Prob(0.8);
//		sm.setMS_Prob(1);
		sm.setEnd(3000);
		return sm;
	}
	
	public int test1()	{
		SLogF.init("test/log2.txt");
		Anal a=new AnalEDF_AD_E();
		TaskSimul_EDF_AD_E ts=new TaskSimul_EDF_AD_E();
//		ts.setRecoverIdle(false);
		
		TaskMng tm=TS_MC3.ts3();
		SysMng sm=getSM();
		a.init(tm);
		sm.setX(a.computeX());
//		sm.prn();
		ts.init_sm_tm(sm,tm);
		ts.simul(0,sm.getEnd());
		ts.simul_end();
		
		SimulInfo si=ts.getSI();
		si.prn();
		SLogF.end();
		return 0;
	}
	
	public int test2() {
		SLogF.init("test/log2.txt");
		Anal a=new AnalEDF_AD_E();
		TaskSimulCom_NA ts=new TaskSimulCom_NA();
//		ts.setRecoverIdle(false);
		
		TaskMng tm=TS_MC3.ts3();
		SysMng sm=getSM();
		a.init(tm);
		sm.setX(a.computeX());
//		sm.prn();
		ts.init_sm_tm(sm,tm);
		ts.simul(0,sm.getEnd());
		ts.simul_end();
		
		SimulInfo si=ts.getSI();
		si.prn();
		SLogF.end();
		return 0;
	}
	public int test3() {
		SLogF.init("test/log2.txt");
		CompMng cm=CompMngEx1.getCompMng3();
		TaskMng tm=cm.getTM();
		cm.part();
//		cm.analMaxRes();
//		tm.prnComp();
		SysMng sm=new SysMng();
		sm.setMS_Prob(0.5);
		sm.setX(AnalEDF_VD.computeX(tm));
		TaskSimulCom_NA ts=new TaskSimulCom_NA();
		ts.init_sm_tm(sm, tm);
		ts.set_cm(cm);
		ts.simul(0,300);
		ts.simul_end();
		SimulInfo si=ts.getSI();
		si.prn();
		SLogF.end();
		return 0;
	}
	public  int test4() {
		return 1;
	}
	public  int test5() {
		return 1;
	}
	public  int test6() {
		return 1;
	}
	public  int test7() {
		return 1;
	}
	public  int test8() {
		return 0;
	}
	public  int test9() {
		return 0;
	}
	public  int test10() {
		return 0;
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = z_TaskSimul4.class;
		z_TaskSimul4 m=new z_TaskSimul4();
		int[] aret=z_TaskSimul4.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}