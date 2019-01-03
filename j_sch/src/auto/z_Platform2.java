package auto;

/*
 * Simul auto develop 
 * 
 */

import anal.Anal;
import anal.AnalSel;
import basic.TaskMng;
import sim.SimulSel;
import sim.SysMng;
import sim.TaskSimul;
import sim.mc.TaskSimul_EDF_VD;
import gen.SysLoad;
import util.Log;
import util.TEngine;
import z_ex.TS_MC1;

public class z_Platform2 {
//	public static int idx=1;
//	public static int idx=2;
//	public static int idx=3;
	public static int idx=4;
//	public static int log_level=1;
	public static int log_level=2;

	//
	public int test1() 
	{
		TaskMng tm=TS_MC1.ts1();
		tm.prnInfo();
		Anal a=AnalSel.getAnal(1);
		a.init(tm);
		a.prepare();
		double x=a.computeX();
		Log.prn(1, "x:"+x);
		SysMng sm=new SysMng();
		sm.setMS_Prob(0.1);
		sm.setX(x);
		TaskSimul_EDF_VD tsim=new TaskSimul_EDF_VD();
		tsim.init_sm_tm(sm,tm);
		tsim.simulBy(0,20);
		tsim.simulEnd();
		return -1;		

	}

	public int test2() 
	{
		SysLoad sy=new SysLoad("sch/t1/taskset_65");
		sy.open();
		TaskMng tm=sy.loadOne();
//		tm.prnInfo();
		Anal a=AnalSel.getAnal(1);
		a.init(tm);
		a.prepare();
		double x=a.computeX();
//		Log.prn(1, "x:"+x);
		SysMng sm=new SysMng();
		sm.setMS_Prob(0.3);
		sm.setX(x);
		TaskSimul_EDF_VD tsim=new TaskSimul_EDF_VD();
		tsim.init_sm_tm(sm,tm);
		tsim.simulBy(0,100);
		tsim.simulEnd();
		return -1;
	}
	
	public int test3() 
	{
		String path="sch/t1/";
		String fn="sch/t1/taskset_65";
		Anal a=AnalSel.getAnal(1);
		TaskSimul s=SimulSel.getSim(1);
		Platform p=new Platform(path);
		p.simul_one(fn,null,a,s);
		return -1;
	}
	
	public  int test4() 
	{
		String path="sch/t1/";
		String ts="a_ts_list.txt";
		Platform p=new Platform(path);
		p.simul(ts,0);
		return -1;
	}
	
	public  int test5() 
	{
		return -1;
	}
	public  int test6() 
	{
		return -1;
	}
	
	public  int test7()
	{
		return -1;
	}
	public  int test8()
	{
		return -1;
	}
	
	public  int test9()
	{
		return 0;
	}
	
	public  int test10()
	{
		return 0;
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = z_Platform2.class;
		z_Platform2 m=new z_Platform2();
		int[] aret=z_Platform2.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
