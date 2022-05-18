package sim.mc;

import anal.Anal;
import anal.AnalEDF_AD_E;
import anal.AnalEDF_IV;
import anal.AnalEDF_IV2;
import anal.AnalEDF_IV4;
import anal.AnalEDF_VD;
import gen.SysLoad;
import imc.AnalEDF_RUN;
import sim.SimulInfo;
import sim.SysMng;
import sim.TaskSimul;
import task.TaskMng;
import util.SEngineT;
import util.SLog;
import util.SLogF;
import z_ex.TS_MC4;

public class z_TaskSimul5 {
	public static int idx=1;
//	public static int idx=2;
//	public static int idx=3;
	public static int log_level=1;


	public int test1()	{
		
		int et=10000;
		
		
		SysLoad sy=new SysLoad("run/pi0/taskset_80");
		sy.open();
		int num=2;
		TaskMng tm=null;
		for(int i=0;i<num;i++) {
			tm=sy.loadOne();
		}
		Anal a;
		TaskSimulMC ts;
//		SLogF.init("test_RU.txt");
//		a=new AnalEDF_RUN();
//		ts=new TaskSimul_MC_RUN();
//		simul(a, ts,tm,et);
//		SLogF.end();

		SLogF.init("test_RU2.txt");
		a=new AnalEDF_RUN();
		ts=new TaskSimul_MC_RUN2();
		simul(a, ts,tm,et);
		SLogF.end();
		
		
//		SLogF.init("test_AD.txt");
//		a=new AnalEDF_AD_E();
//		ts=new TaskSimul_EDF_AD_E();
//		simul(a, ts,tm,et);
//		SLogF.end();

		return 0;
	}
	private void simul(Anal a, TaskSimulMC ts,TaskMng tm,int et) {
		SLog.prn(2, "--!!!----------");
		a.init(tm);
		a.prepare();
		double d=a.getDtm();
		SLog.prn(2, "det:"+d);
		SysMng sm=new SysMng();
//		SLogF.init("test.txt");
		sm.setMS_Prob(0.3);
		double x=a.computeX();
		sm.setX(x);
//		ts=new TaskSimul_EDF_AD_E();
		ts.init_sm_tm(sm,tm);
		tm.prnTxt();
		ts.simul(0,et);
		ts.simul_end();
		SimulInfo si=ts.getSI();
		si.prn();
//		SLogF.end();
		
	}

	
	public int test2() {
////		int no=1;
////		int no=2;
//		int no=3;
////		int no=4;
////		int no=5;
////		int no=6;
////		int no=7;
//		TaskMng tm=TS_MC4.getTS(no);
		
		int et=2000;
		
		SysLoad sy=new SysLoad("ind/t2/taskset_95");
		sy.open();
		int num=3;
		TaskMng tm=null;
		for(int i=0;i<num;i++) {
			tm=sy.loadOne();
		}
		Anal a;

		
		a=new AnalEDF_AD_E();
		a.init(tm);
		a.prepare();
		double d=a.getDtm();
		SLog.prn(2, "det:"+d);
		SysMng sm=new SysMng();
		SLogF.init("test.txt");
		sm.setMS_Prob(0.3);
		double x=a.computeX();
		sm.setDelay(x*tm.getLongPeriod());		
		sm.setX(x);
		SLog.prn(2, "x:"+x);
		TaskSimul_EDF_Post2 ts=new TaskSimul_EDF_Post2();
		ts.init_sm_tm(sm,tm);
		tm.prnTxt();
		tm.prnOffline();
		ts.simul(0,et);
		ts.simul_end();
		SimulInfo si=ts.getSI();
		si.prn();
		
		SLogF.end();
		return 0;
	}
	public int test3() {
//		int no=1;
//		int no=2;
//		int no=3;
		int no=4;
//		int no=5;
//		int no=6;
//		int no=7;
//		int no=8;
		TaskMng tm=TS_MC4.getTS(no);
		
		int et=2000;
		
		Anal a;
		double d;

//		a=new AnalEDF_VD();
//		a.init(tm);
//		a.prepare();
//		d=a.getDtm();
//		SLog.prn(2, "det:"+d);
	
		
		a=new AnalEDF_IV2();
		a.init(tm);
		a.prepare();
		d=a.getDtm();
		SLog.prn(2, "det:"+d);
		SysMng sm=new SysMng();
		SLogF.init("test.txt");
		sm.setMS_Prob(0.3);
		double x=a.computeX();
		sm.setX(x);
		TaskSimul_EDF_IV ts=new TaskSimul_EDF_IV();
		ts.init_sm_tm(sm,tm);
		tm.prnTxt();
		ts.simul(0,et);
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
		Class c = z_TaskSimul5.class;
		z_TaskSimul5 m=new z_TaskSimul5();
		int[] aret=z_TaskSimul5.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}