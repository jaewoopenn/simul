package sim.mc;

import sim.SimulInfo;
import sim.SysMng;
import task.TaskMng;
import util.SEngineT;
import util.SLog;
import util.SLogF;
import z_ex.TS_MC3;

public class z_TaskSimul2 {
//	public static int idx=1;
//	public static int idx=2;
	public static int idx=3;
	public static int log_level=1;


	public SysMng getSM() {
		SysMng sm=new SysMng();
		sm.setMS_Prob(0.5);
		sm.setX(1.0/3);
		sm.setEnd(1000);
		return sm;
	}
	
	public int test1()	{
		SLogF.init("test/log1.txt");
		TaskSimul_EDF_Post ts=new TaskSimul_EDF_Post();
		ts.setRecoverIdle(false);
		
		TaskMng tm=TS_MC3.ts2();
		SysMng sm=getSM();
		sm.setDelay(tm.getLongPeriod()*sm.getX());
//		sm.setDelay(0);
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
		TaskSimul_EDF_AD_E ts=new TaskSimul_EDF_AD_E();
		ts.setRecoverIdle(false);
		
		TaskMng tm=TS_MC3.ts2();
		SysMng sm=getSM();
//		sm.prn();
		ts.init_sm_tm(sm,tm);
		ts.simul(0,sm.getEnd());
		ts.simul_end();
		
		SimulInfo si=ts.getSI();
		si.prn();
		SLogF.end();
		return 0;
	}
	
	public  int test3()	{
		TaskSimul_EDF_Post ts=new TaskSimul_EDF_Post();
//		ts.setRecoverIdle(false);
		
		SLogF.init("test/log1.txt");
		TaskMng tm=TS_MC3.ts2();
		SysMng sm=getSM();
		sm.setDelay(tm.getLongPeriod()*sm.getX());
//		sm.setDelay(0);
//		sm.prn();
		ts.init_sm_tm(sm,tm);
		ts.simul(0,sm.getEnd());
		ts.simul_end();
		SimulInfo si=ts.getSI();
		si.prn();
		SLogF.end();
		
		SLog.prn(1, "-----------");
		tm.init();
		SLogF.init("test/log2.txt");
		TaskSimul_EDF_AD_E ts2=new TaskSimul_EDF_AD_E();
//		ts2.setRecoverIdle(false);
		ts2.init_sm_tm(sm,tm);
		ts2.simul(0,sm.getEnd());
		ts2.simul_end();
		
		si=ts2.getSI();
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
		Class c = z_TaskSimul2.class;
		z_TaskSimul2 m=new z_TaskSimul2();
		int[] aret=z_TaskSimul2.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}
