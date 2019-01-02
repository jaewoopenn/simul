package sim;

import basic.TaskMng;
import sim.mc.TaskSimul_EDF_VD;
import util.TEngine;
import z_ex.TS_MC1;
import z_ex.TS_NonMC1;

public class z_TaskSimul1 {
//	public static int idx=1;
	public static int idx=2;
	public static int log_level=1;
	public static int gret[]={1,0,1,0,-1, -1,-1,-1,-1,-1};


	public int test1()	{
		TaskSimul ts=new TaskSimul();
		ts.init_sm_tm(null,TS_NonMC1.ts1());
		ts.simulBy(0,20);
		ts.simulEnd();
		return 0;
	}
	public int test2() {
		int et=40;
		TaskSimul_EDF_VD ts=new TaskSimul_EDF_VD();
		SysMng sm=new SysMng();
		sm.setMS_Prob(0.3);
		sm.setX(0.2);
//		TaskMng tm=TS_NonMC1.ts1();
		TaskMng tm=TS_MC1.ts1();
		ts.init_sm_tm(sm,tm);
		ts.simulBy(0,et);
		ts.simulEnd();
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
		Class c = z_TaskSimul1.class;
		z_TaskSimul1 m=new z_TaskSimul1();
		int[] aret=z_TaskSimul1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}