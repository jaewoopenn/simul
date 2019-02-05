package sim.mc;
/*
 * TODO make MC-Post scheduling algorithm in simulation
 * 
 * 
 */

import anal.Anal;
import anal.AnalSel;
import basic.TaskMng;
import sim.SysMng;
import util.FLog;
import util.Log;
import util.TEngine;
import z_ex.TS_MC2;

public class z_TaskSimul_post {
	public static int idx=1;
	public static int log_level=1;
	public static int gret[]={1,0,1,0,-1, -1,-1,-1,-1,-1};


	public int test1()	{
		int et=1000;
		SysMng sm=new SysMng();
		sm.setMS_Prob(0.3);
		TaskMng tm=TS_MC2.ts1();
		Anal a=AnalSel.getAnal(0);
		a.init(tm);
		a.prepare();
		if(!a.isScheduable()) {
			Log.err("not schedulable");
			return -1;
		}
		double x=a.computeX();
		Log.prn(1, "x:"+x);
		sm.setX(x);
		
		FLog.init("sch/log.txt");
		TaskSimul_EDF_Post ts=new TaskSimul_EDF_Post();
		ts.init_sm_tm(sm,tm);
		ts.simulBy(0,et);
		ts.simulEnd();
		FLog.end();
		return 0;
	}
	public int test2() {
		return 0;
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
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
