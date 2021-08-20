package sim.mc;

import anal.Anal;
import anal.AnalEDF_AD_E;
import anal.AnalEDF_IV;
import gen.SysLoad;
import sim.SimulInfo;
import sim.SysMng;
import task.TaskMng;
import util.SEngineT;
import util.SLog;
import util.SLogF;
import z_ex.TS_MC4;

public class z_TaskSimul5 {
	public static int idx=1;
//	public static int idx=2;
	public static int log_level=1;


	public int test1()	{
////		int no=1;
////		int no=2;
//		int no=3;
////		int no=4;
////		int no=5;
////		int no=6;
////		int no=7;
//		TaskMng tm=TS_MC4.getTS(no);
		
		int et=10000;
		
		
		SysLoad sy=new SysLoad("ind/p2/taskset_55");
		sy.open();
		int num=45;
		TaskMng tm=null;
		for(int i=0;i<num;i++) {
			tm=sy.loadOne();
		}
		Anal a;

		
		a=new AnalEDF_IV();
		a.init(tm);
		a.prepare();
		double d=a.getDtm();
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