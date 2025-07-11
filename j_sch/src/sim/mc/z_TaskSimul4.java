package sim.mc;

import sim.SimulInfo;
import sim.SysMng;
import task.TaskMng;
import util.SEngineT;
import util.SLogF;
import z_ex.TS_MC3;

public class z_TaskSimul4 {
//	public static int idx=1;
	public static int idx=2;
	public static int log_level=1;


	public SysMng getSM() {
		SysMng sm=new SysMng();
//		sm.setMS_Prob(0.0);
		sm.setMS_Prob(0.5);
//		sm.setMS_Prob(0.8);
//		sm.setMS_Prob(1);
//		sm.setX(0.5);
		sm.setX(0.1939835755127518);
		sm.setEnd(3000);
		return sm;
	}
	
	public int test1()	{
		SLogF.init("test/log2.txt");
		TaskSimul_EDF_AD_E ts=new TaskSimul_EDF_AD_E();
		ts.setRecoverIdle(false);
		
		TaskMng tm=TS_MC3.ts3();
		SysMng sm=getSM();
//		sm.prn();
		ts.init_sm_tm(sm,tm);
		ts.simul(sm.getEnd());
		
		SimulInfo si=ts.getSI();
		si.prn();
		SLogF.end();
		return 0;
	}
	
	public int test2() {
		SLogF.init("test/log2.txt");
		TaskSimul_FMC ts=new TaskSimul_FMC();
//		ts.setRecoverIdle(false);
		
//		TaskMng tm=TS_MC3.ts3();
		TaskMng tm=TS_MC3.ts4();
		SysMng sm=getSM();
//		sm.prn();
		ts.init_sm_tm(sm,tm);
		ts.simul(sm.getEnd());
		
		SimulInfo si=ts.getSI();
		si.prn();
		SLogF.end();
		return -1;
	}
	public int test3() {
		SLogF.init("test/log2.txt");
		TaskSimul_FMC ts=new TaskSimul_FMC();
//		ts.setRecoverIdle(false);
		
//		TaskMng tm=TS_MC3.ts3();
		TaskMng tm=TS_MC3.ts4();
		SysMng sm=getSM();
//		sm.prn();
		ts.init_sm_tm(sm,tm);
		ts.simul(sm.getEnd());
		ts.simul_end();
		
		SimulInfo si=ts.getSI();
		si.prn();
		SLogF.end();
		return -1;
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