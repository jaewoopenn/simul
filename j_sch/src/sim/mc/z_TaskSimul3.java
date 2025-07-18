package sim.mc;

import anal.Anal;
import anal.AnalSel;
import gen.SysLoad;
import sim.SimulInfo;
import sim.SysMng;
import task.TaskMng;
import util.SEngineT;
import util.SLogF;

public class z_TaskSimul3 {
//	public static int idx=1;
	public static int idx=2;
//	public static int idx=3;
	public static int log_level=1;


	public SysMng getSM() {
		SysMng sm=new SysMng();
		sm.setMS_Prob(0.75);
//		sm.setX(1.0/3);
//		sm.setEnd(3000);
		sm.setEnd(30000);
		return sm;
	}
	
	public int test1()	{
		SLogF.init("test/log2.txt");
		TaskSimul_EDF_Post ts=new TaskSimul_EDF_Post();

		SysMng sm=getSM();

		SysLoad sy=new SysLoad("sch/t3/taskset.txt");
		sy.open();
		TaskMng tm=sy.loadOne();
		
//		SysLoad sy=new SysLoad("sch/t2/taskset_85");
//		sy.open();
//		int num=326;
//		TaskMng tm=null;
//		for(int i=0;i<num;i++) {
//			tm=sy.loadOne();
//		}
//		tm.prn();
//		tm.prnTxt();
		Anal a=AnalSel.getAnal(0);
		a.init(tm);
		
		sm.setX(a.computeX());
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
		TaskSimul_EDF_Post ts=new TaskSimul_EDF_Post();

		SysMng sm=getSM();

		
//		SysLoad sy=new SysLoad("sch/p2/taskset_95");
		SysLoad sy=new SysLoad("sch/t3/taskset.txt");
		sy.open();
		int num=1;
//		int num=3160;
		TaskMng tm=null;
		for(int i=0;i<num;i++) {
			tm=sy.loadOne();
		}
//		tm.prnTxt();
		Anal a=AnalSel.getAnal(0);
		a.init(tm);
//		a.prn();
//		SLog.prn(1, a.computeX());
		double x=a.computeX();
		sm.setX(x);
		sm.setDelay(x*tm.getLongPeriod());
		ts.setBE();
		ts.init_sm_tm(sm,tm);
		ts.simul(sm.getEnd());
		
		SimulInfo si=ts.getSI();
		si.prn();
		SLogF.end();
		return 0;
	}
	
	public  int test3()	{
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
		Class c = z_TaskSimul3.class;
		z_TaskSimul3 m=new z_TaskSimul3();
		int[] aret=z_TaskSimul3.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}
