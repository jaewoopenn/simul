package sim.mc;
/*
 * TODO make MC-Post scheduling algorithm in simulation
 * 
 * 
 */

import anal.Anal;
import anal.AnalSel;
import basic.TaskMng;
import sim.SimulInfo;
import sim.SysMng;
import util.S_FLog;
import util.S_Log;
import util.S_TEngine;
import z_ex.TS_MC2;

public class z_TaskSimul_post {
	public static int idx=2;
	public static int log_level=1;
	public static int gret[]={1,0,1,0,-1, -1,-1,-1,-1,-1};

	private int g_et;
	private double g_prob;
	private SimulInfo g_si;
	
	public void init_g() {
		g_et=200;
//		g_et=1000;
		
//		g_prob=0.3;
		g_prob=0.5;
//		g_prob=1.0;

		S_FLog.init("sch/log.txt");
//		S_FLog.initScr();
	}

	public int end_g() {
		g_si.prn();
		S_FLog.end();
		return 0;
	}

	public int test1()	{
		init_g();
		TaskMng tm=TS_MC2.ts2();
		
		SysMng sm=new SysMng();
		sm.setMS_Prob(g_prob);
		
		
		Anal a=AnalSel.getAnal(0);
		a.init(tm);
		a.prepare();
		a.proceed_if_sch();
		
		double x=a.computeX();
		S_Log.prn(1, "x:"+x);
		sm.setX(x);
//		tm.prn();
//		System.exit(1);
		
		TaskSimul_EDF_Post ts=new TaskSimul_EDF_Post();
		ts.init_sm_tm(sm,tm);
		ts.simul(0,g_et);
		ts.simul_end();
		
		g_si=ts.getSI();
		return 	end_g();		

	}
	
	public int test2() {
		init_g();
		TaskMng tm=TS_MC2.ts2();
		double x=0.7686274509803924;
		
		SysMng sm=new SysMng();
		sm.setMS_Prob(g_prob);
		sm.setX(x);
		
		TaskSimul_EDF_Post ts=new TaskSimul_EDF_Post();
		ts.init_sm_tm(sm,tm);
		ts.simul(0,g_et);
		ts.simul_end();
		
		g_si=ts.getSI();
		return 	end_g();		
	}
	
	public  int test3()	{
		return 0;
	}
	
	public  int test4()	{
		return 0;
	}
	
	public  int test5() {
		return -1;
	}
	public  int test6()	{
		return -1;
	}
	public  int test7()	{
		return -1;
	}
	public  int test8()	{
		return -1;
	}
	public  int test9() {
		return -1;
	}
	public  int test10() {
		return -1;
	}



	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = z_TaskSimul_post.class;
		z_TaskSimul_post m=new z_TaskSimul_post();
		int[] aret=z_TaskSimul_post.gret;
		if(idx==-1)
			S_TEngine.run(m,c,aret,10);
		else
			S_TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
