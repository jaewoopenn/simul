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

public class z_TaskSimul6 {
	public static int idx=1;
//	public static int idx=2;
//	public static int idx=3;
//	public static int idx=4;
	public static int log_level=2;


	public int test1()	{
		double prob=0.7;
		int et=10000;
		
		
		SysLoad sy=new SysLoad("ind/p2/taskset_95");
		sy.open();
		int num=500;
		TaskMng tm=null;
		double r1s=0,r2s=0;
		for(int i=0;i<num;i++) {
			tm=sy.loadOne();
			Anal a;
			a=new AnalEDF_IV();
			a.init(tm);
			double d=a.getDtm();
			SysMng sm=new SysMng();
			sm.setMS_Prob(prob);
			double x=a.computeX();
			sm.setX(x);
			TaskSimul_EDF_IV ts=new TaskSimul_EDF_IV();
			ts.init_sm_tm(sm,tm);
			ts.setBE();
			ts.simul(0,et);
			ts.simul_end();
			SimulInfo si=ts.getSI();
			double r1=si.getDMR();

			
			
			a=new AnalEDF_AD_E();
			a.init(tm);
			d=a.getDtm();
			sm=new SysMng();
			sm.setMS_Prob(prob);
			x=a.computeX();
			sm.setDelay(x*tm.getLongPeriod());		
			sm.setX(x);
			TaskSimul_EDF_Post2 ts2=new TaskSimul_EDF_Post2();
			ts2.init_sm_tm(sm,tm);
			ts2.setBE();
			ts2.simul(0,et);
			ts2.simul_end();
			si=ts2.getSI();
			double r2=si.getDMR();
			SLog.prnc(2, "no:"+i);
			SLog.prnc(2, " iv:"+r1);
			if(r1>r2) {
				SLog.prnc(2, ">");
			}
			else
			{
				SLog.prnc(2, "<=");
			}
			SLog.prn(2, " post2:"+r2);
			r1s+=r1;
			r2s+=r2;
		}
		SLog.prnc(2, "iv:"+(r1s/100));
		SLog.prn(2, " post2:"+(r2s/100));
		
		return 0;
	}
	
	public int test2() {
		
		SysLoad sy=new SysLoad("ind/p2/taskset_95");
		sy.open();
		int num=500;
		TaskMng tm=null;
		double r1s=0,r2s=0;
		for(int i=0;i<num;i++) {
			tm=sy.loadOne();
			Anal a;
			a=new AnalEDF_IV();
			a.init(tm);
			double d=a.getDtm();
			SLog.prnc(2, "iv:"+d);

			
			
			a=new AnalEDF_AD_E();
			a.init(tm);
			d=a.getDtm();
			SLog.prn(2, " post2:"+d);

			
			
		}
		
		return 0;
	}
	public int test3() {
		SysLoad sy=new SysLoad("ind/p2/taskset_55");
		sy.open();
		int num=500;
		TaskMng tm=null;
		double r1s=0,r2s=0;
		for(int i=0;i<num;i++) {
			tm=sy.loadOne();
			Anal a;
			double d;
		
			
			a=new AnalEDF_AD_E();
			a.init(tm);
			d=a.getDtm();
			SLog.prn(2, " post2:"+d);
			
			
		}
		return 0;
	}
	public  int test4() {
		double prob=0.7;
		int et=1000;
		
		
		SysLoad sy=new SysLoad("ind/p2/taskset_95");
		sy.open();
		int num=500;
		int no=69;
		TaskMng tm=null;
		SLogF.init("test.txt");
		for(int i=0;i<num;i++) {
			tm=sy.loadOne();
			if (i!=no)
				continue;
			Anal a;
			SysMng sm;
			double d,x;
			SimulInfo si;
			double r1,r2;
//			a=new AnalEDF_IV();
//			a.init(tm);
//			a.prepare();
//			d=a.getDtm();
//			sm=new SysMng();
//			sm.setMS_Prob(prob);
//			x=a.computeX();
//			sm.setX(x);
//			TaskSimul_EDF_IV ts=new TaskSimul_EDF_IV();
//			ts.init_sm_tm(sm,tm);
//			ts.setBE();
//			ts.simul(0,et);
//			ts.simul_end();
//			si=ts.getSI();
//			r1=si.getDMR();

			a=new AnalEDF_AD_E();
			a.init(tm);
			d=a.getDtm();
			sm=new SysMng();
			sm.setMS_Prob(prob);
			x=a.computeX();
			sm.setDelay(x*tm.getLongPeriod());		
			sm.setX(x);
			TaskSimul_EDF_Post2 ts2=new TaskSimul_EDF_Post2();
			ts2.init_sm_tm(sm,tm);
			ts2.setBE();
			ts2.simul(0,et);
			ts2.simul_end();
			si=ts2.getSI();
			r1=si.getDMR();
			
			SLog.prnc(2, "no:"+i);
			SLog.prn(2, " iv:"+r1);
		}
		SLogF.end();
		
		return 0;
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
		Class c = z_TaskSimul6.class;
		z_TaskSimul6 m=new z_TaskSimul6();
		int[] aret=z_TaskSimul6.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}