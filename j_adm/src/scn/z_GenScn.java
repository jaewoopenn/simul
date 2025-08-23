package scn;

import gen.SysLoad;
import task.TaskMng;
import util.SEngineT;
import util.SLog;

public class z_GenScn {
//	public static int idx=1;
	public static int idx=2;
//	public static int idx=3;
	public static int log_level=1;


	public int test1()	{
		String ts="adm/test1/taskset_96";
		String out="adm/test.txt";
		int n=10;
		double prob=0.7;
		int dur=500;
		SysLoad sy=new SysLoad(ts);
		String ret=sy.open();
		SLog.prn(1, ret);
		sy.moveto(n);
//		TaskMng tm=sy.loadOne2();
//		GenScn gs=new GenScn();
//		gs.setProb(prob);
//		gs.gen(tm,dur,out);

		
		return 0;
	}
	
	public int test2() {
		String in="adm/test.txt";
		GenScn gs=new GenScn();
		gs.play(in);
		return -1;
	}
	public int test3() {
		String ts="adm/test1/taskset_96";
		int n=10;
		SysLoad sy=new SysLoad(ts);
		String ret=sy.open();
		SLog.prn(1, ret);
		sy.moveto(n);
//		TaskMng tm=sy.loadOne();
//		tm.prn();
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