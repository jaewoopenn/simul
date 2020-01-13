package sim;

/*
 * test edf post
 */

import sim.mc.TaskSimul_EDF_Post;
import task.TaskMng;
import util.SEngineT;
import util.SLogF;
import z_ex.TS_MC1;

public class z_TaskSimul2 {
	public static int idx=1;
//	public static int idx=2;
//	public static int idx=3;
	public static int log_level=1;


	public SysMng getSM() {
		SysMng sm=new SysMng();
		sm.setMS_Prob(0.6);
		sm.setX(1.0/3);
		return sm;
	}
	
	public int test1()	{
		int et=40;
		SLogF.init("test/log.txt");
		TaskSimul_EDF_Post ts=new TaskSimul_EDF_Post();
		ts.g_recover_on=false;
		TaskMng tm=TS_MC1.ts1();
		ts.init_sm_tm(getSM(),tm);
		ts.simul(0,et);
		ts.simul_end();
		
		SimulInfo si=ts.getSI();
		si.prn();
		SLogF.end();
		return 0;
	}
	public int test2() {
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
		Class c = z_TaskSimul2.class;
		z_TaskSimul2 m=new z_TaskSimul2();
		int[] aret=z_TaskSimul2.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}
