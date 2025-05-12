package imc;

import gen.SysLoad;
import sim.SysMng;
import sim.mc.TaskSimul_EDF_VD;
import task.Task;
import task.TaskMng;
import util.SEngineT;
import util.SLog;
import z_ex.TS_MC1;

public class z_GenScn {
	public static int idx=1;
	public static int log_level=1;


	public int test1()	{
		String ts="adm/test1/taskset_96";
		int n=10;
		int dur=300;
		SysLoad sy=new SysLoad(ts);
		String ret=sy.open();
		SLog.prn(1, ret);
		sy.moveto(n);
		TaskMng tm=sy.loadOne();
		GenScn gs=new GenScn(tm);
		gs.loop(dur);

		
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
		Class c = z_GenScn.class;
		z_GenScn m=new z_GenScn();
		int[] aret=z_GenScn.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}