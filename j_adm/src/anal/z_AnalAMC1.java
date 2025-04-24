package anal;

import z_ex.TS_MC6;
import task.TaskMng;
import util.SEngineT;
import util.SLog;

public class z_AnalAMC1 {
	public static int idx=2;
	public static int log_level=1;

	public int test1() 
	{
//		TaskMng tm=TS_MC1.ts1();
//		TaskMng tm=TS_MC6.ts1();
		TaskMng tm=TS_MC6.ts2();
		Anal a=new AnalAMC();
		a.init(tm);
		a.prepare();
		if(a.getDtm()<=1) {
			SLog.prn(1, "OK");
		} else {
			SLog.prn(1, "Not OK");
			
		}
		a.prn();
		return -1;
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
		Class c = z_AnalAMC1.class;
		z_AnalAMC1 m=new z_AnalAMC1();
		int[] aret=z_AnalAMC1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
