package sim.mc;

import anal.Anal;
import anal.AnalEDF_AD_E;
import anal.AnalEDF_IV;
import anal.AnalEDF_IV2;
import anal.AnalEDF_IV3;
import anal.AnalEDF_IV4;
import anal.AnalEDF_VD;
import gen.SysLoad;
import sim.SimulInfo;
import sim.SysMng;
import sim.TaskSimul;
import task.TaskMng;
import util.SEngineT;
import util.SLog;
import util.SLogF;
import z_ex.TS_MC4;
import z_ex.TS_MC5;

public class z_TaskSimul7 {
//	public static int idx=1;
//	public static int idx=2;
	public static int idx=3;
	public static int log_level=2;


	public int test1()	{
		
	
		
		SysLoad sy=new SysLoad("ind/p0/taskset_90");
		sy.open();
		int num=20;
		TaskMng tm=null;
		for(int i=0;i<num;i++) {
			tm=sy.loadOne();
		}
		Anal a;
		double d;
		
		a=new AnalEDF_IV4();
		a.init(tm);
		a.prepare();
		d=a.getDtm();
		SLog.prn(2, "RUN det:"+d);
		
		a=new AnalEDF_AD_E();
//		a=new AnalEDF_IV3();
		a.init(tm);
		a.prepare();
		d=a.getDtm();
		SLog.prn(2, "ADE det:"+d);

		return 0;
	}

	
	public int test2() {
		
		
		SysLoad sy=new SysLoad("ind/t1/taskset_90");
		sy.open();
		int num=20;
		TaskMng tm=null;
		Anal a;
		double d;
		for(int i=0;i<num;i++) {
			tm=sy.loadOne();
			a=new AnalEDF_IV4();
			a.init(tm);
			a.prepare();
			d=a.getDtm();
			SLog.prn(2, "RUN det:"+d);
			
			a=new AnalEDF_AD_E();
//			a=new AnalEDF_IV3();
			a.init(tm);
			a.prepare();
			d=a.getDtm();
			SLog.prn(2, "ADE det:"+d);
			SLog.prn(2, "---------");
		}
		

		return 0;
	}
	public int test3() {
		
		int et=10000;
		
		
		TaskMng tm=TS_MC5.ts6();
		tm.prnInfo();

//		.... 0.779*0.4818+0.6327=1.0008
//		... 0.762*0.4818+0.6327=0.9998
		
		Anal a;
		TaskSimulMC ts;
		SLogF.init("test_RU.txt");
		a=new AnalEDF_IV4();
		ts=new TaskSimul_MC_RUN();
		simul(a, ts,tm,et);
		SLogF.end();


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
		Class c = z_TaskSimul7.class;
		z_TaskSimul7 m=new z_TaskSimul7();
		int[] aret=z_TaskSimul7.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}