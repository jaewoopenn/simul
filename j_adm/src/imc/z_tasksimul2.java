package imc;

import gen.SysLoad;
import sim.*;
import task.DTaskVec;
import util.SEngineT;
import util.SLog;
import util.SLogF;

public class z_tasksimul2 {
	public static int idx=1;
//	public static int idx=2;
	public static int log_level=1;


	public int test1()	{
		String tsn="adm/test1/taskset_96";
		String scn="adm/test.txt";
		int n=10;
		int dur=350;
		SysLoad sy=new SysLoad(tsn);
		String ret=sy.open();
		SLog.prn(1, ret);
		sy.moveto(n);
		DTaskVec tm=sy.loadOne2();

		SysMng sm=new SysMng();
		sm.setMS_Prob(0);
		sm.setX(0.9);
		
		TaskSimul_IMC ts=new TaskSimul_EDF_VD_IMC();
		ts.init_sm_dt(sm,tm);
//		ts.setScn(scn);

//		SLogF.init(out);
		ts.simul(dur);
		ts.simul_end();
		SimulInfo si=ts.getSI();
		si.prn();
//		SLogF.save();
		return 0;
	}
	
	public int test2() {
		return -1;
	}
	public int test3() {
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
		Class c = z_tasksimul2.class;
		z_tasksimul2 m=new z_tasksimul2();
		int[] aret=z_tasksimul2.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}