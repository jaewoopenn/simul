package imc;

import anal.Anal;
import anal.AnalEDF_AD_E;
import anal.AnalEDF_IV;
import anal.AnalEDF_IV2;
import anal.AnalEDF_IV4;
import anal.AnalEDF_VD;
import gen.SysLoad;
import imc.AnalEDF_RUN;
import imc.TaskSimul_IMC;
import imc.TaskSimul_MC_RUN;
import imc.TaskSimul_MC_RUN2;
import sim.SimulInfo;
import sim.SysMng;
import sim.TaskSimul;
import task.TaskMng;
import util.SEngineT;
import util.SLog;
import util.SLogF;
import z_ex.TS_MC4;

public class z_TaskSimul1 {
	public static int idx=1;
//	public static int idx=2;
//	public static int idx=3;
	public static int log_level=2;


	public int test1()	{
		
		int et=10000;
		
		
		SysLoad sy=new SysLoad("run/pi0/taskset_96");
		sy.open();
		int num=308;
		TaskMng tm=null;
		for(int i=0;i<num;i++) {
			tm=sy.loadOne();
		}
		tm.prn();
		Anal a;
		TaskSimul_IMC ts;
		SLogF.init("test_RU.txt");
		a=new AnalEDF_RUN();
		ts=new TaskSimul_MC_RUN();
//		ts.setRecoverIdle(false);
		simul(a, ts,tm,et);
		SLogF.end();
		

		return 0;
	}
	private void simul(Anal a, TaskSimul_IMC ts,TaskMng tm,int et) {
		SLog.prn(2, "--!!!----------");
		a.init(tm);
		a.prepare();
		a.prn();
		double d=a.getDtm();
		SLog.prn(2, "det:"+d);
		SysMng sm=new SysMng();
		sm.setMS_Prob(0.5);
		double x=a.computeX();
		sm.setX(x);
//		sm.setLife(0);
		sm.setLife(2);
		ts.init_sm_tm(sm,tm);
		ts.simul(0,et);
		ts.simul_end();
		SimulInfo si=ts.getSI();
		si.prn();
		
	}

	
	public int test2() {
		SysLoad sy=new SysLoad("run/pi0/taskset_92");
		sy.open();
		int num=420;
		TaskMng tm=null;
		for(int i=0;i<num;i++) {
			tm=sy.loadOne();
		}
		tm.prn();
		Anal a;
		a=new AnalEDF_RUN();
		a.init(tm);
		a.prepare();
		a.prn();
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
		Class c = z_TaskSimul1.class;
		z_TaskSimul1 m=new z_TaskSimul1();
		int[] aret=z_TaskSimul1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}