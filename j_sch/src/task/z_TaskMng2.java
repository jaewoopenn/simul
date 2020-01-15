package task;

import util.SEngineT;
import util.SLog;
import z_ex.TS_MC2;

public class z_TaskMng2 {
	public static int log_level=1;
	public static int idx=2;
	public int test1()
	{
		TaskMng tm=TS_MC2.ts1();
		tm.prn();
		int l=tm.getLongPeriod();
		SLog.prn(1, "p:"+l);
		return -1;
	}
	public int test2() //vd
	{
		TaskMng tm=TS_MC2.ts1();
		tm.prn();
		int l=tm.getShortPeriod();
		SLog.prn(1, "p:"+l);
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
		Class c = z_TaskMng2.class;
		z_TaskMng2 m=new z_TaskMng2();
		int[] aret=z_TaskMng2.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}