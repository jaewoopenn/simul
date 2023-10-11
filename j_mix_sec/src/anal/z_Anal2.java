package anal;

import task.TaskMng;
import util.SEngineT;
import util.SLog;
import z_ex.TS_MC1;
import z_ex.TS_MC2;

public class z_Anal2 {
//	public static int idx=1;
	public static int idx=1;
	public static int log_level=1;

	public int test1() 
	{
//		TaskMng tm=TS_MC2.ts1();
//		TaskMng tm=TS_MC1.ts2();
//		TaskMng tm=TS_MC1.ts3();
//		TaskMng tm=TS_MC2.ts4();
//		TaskMng tm=TS_MC2.ts5();
		TaskMng tm=TS_MC2.ts6();
//		TaskMng tm=TS_MC2.ts7();
		tm.prn();
//		Anal a=new AnalSMC_np();
//		Anal a=new AnalSMC_np2();
		Anal a=new AnalOPA_np();
		a.init(tm);
		a.prepare();
		if(a.getDtm()<=1) {
			SLog.prn(1, "OK");
			a.prn();
		} else {
			SLog.prn(1, "Not OK");
			
		}
		return -1;
	}

	public int test2() {
		TaskMng tm=TS_MC2.ts1();
		tm.prn();
		Anal a=new AnalRM_np(0);
		a.init(tm);
		a.prepare();
		if(a.getDtm()<=1) {
			SLog.prn(1, "OK");
			a.prn();
		} else {
			SLog.prn(1, "Not OK");
			
		}
		return -1;
	}
	public int test3() {
		return -1;

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
		Class c = z_Anal2.class;
		z_Anal2 m=new z_Anal2();
		int[] aret=z_Anal2.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
