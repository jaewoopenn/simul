package sim.mc;

import basic.TaskMng;
import sim.SysMng;
import sim.mc.TaskSimul_EDF_VD;
import util.SEngineT;
import z_ex.TS_MC1;

public class z_TaskSimul1 {
	public static int idx=1;
	public static int log_level=1;
	public static int gret[]={1,0,1,0,-1, -1,-1,-1,-1,-1};


	public int test1()	{
		int et=40;
		SysMng sm=new SysMng();
		sm.setMS_Prob(0.3);
		sm.setX(0.5);
		TaskMng tm=TS_MC1.ts1();
		TaskSimul_EDF_VD ts=new TaskSimul_EDF_VD();
		ts.init_sm_tm(sm,tm);
		ts.simul(0,et);
		ts.simul_end();
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
		Class c = z_TaskSimul1.class;
		z_TaskSimul1 m=new z_TaskSimul1();
		int[] aret=z_TaskSimul1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}

}
