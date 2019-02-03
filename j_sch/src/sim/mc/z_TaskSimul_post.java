package sim.mc;
/*
 * TODO make MC-Post scheduling algorithm in simulation
 */

import basic.TaskMng;
import sim.SysMng;
import sim.mc.TaskSimul_EDF_VD;
import util.TEngine;
import z_ex.TS_MC1;

public class z_TaskSimul_post {
	public static int idx=1;
	public static int log_level=1;
	public static int gret[]={1,0,1,0,-1, -1,-1,-1,-1,-1};


	public int test1()	{
		int et=40;
		SysMng sm=new SysMng();
		sm.setMS_Prob(0.3);
		sm.setX(0.5);
		TaskMng tm=TS_MC1.ts1();
		// anal 
		
		TaskSimul_EDF_VD ts=new TaskSimul_EDF_VD();
		ts.init_sm_tm(sm,tm);
		ts.simulBy(0,et);
		ts.simulEnd();
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
