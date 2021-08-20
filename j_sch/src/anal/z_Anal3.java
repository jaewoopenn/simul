package anal;

/*
 * EDF-IV
 * 
 */

import z_ex.TS_MC1;
import z_ex.TS_MC3;
import z_ex.TS_MC4;
import task.TaskMng;
import util.SEngineT;

public class z_Anal3 {
//	public static int idx=1;
	public static int idx=2;
	public static int log_level=1;

	public int test1() 
	{
		TaskMng tm=TS_MC4.ts1();
		Anal a=new AnalEDF_AD_E();
		a.init(tm);
		a.prepare();
		a.prn();
		return -1;
	}

	public int test2() 
	{
		int no=1;
//		int no=2;
//		int no=3;
//		int no=4;
//		int no=5;
//		int no=6;
//		int no=7;
		
		
		
		TaskMng tm=TS_MC4.getTS(no);
		Anal a;

//		a=new AnalEDF_AD_E();
//		a.init(tm);
//		a.prepare();
//		a.prn();
		
		a=new AnalEDF_IV();
		a.init(tm);
		a.prepare();
		a.prn();
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
		Class c = z_Anal3.class;
		z_Anal3 m=new z_Anal3();
		int[] aret=z_Anal3.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
