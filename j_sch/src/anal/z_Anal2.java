package anal;

import z_ex.TS_MC1;
import z_ex.TS_MC3;
import task.TaskMng;
import util.SEngineT;

public class z_Anal2 {
	public static int idx=1;
//	public static int idx=2;
	public static int log_level=1;

	public int test1() 
	{
//		TaskMng tm=TS_MC1.ts6();
//		TaskMng tm=TS_MC1.ts1();
//		TaskMng tm=TS_MC3.ts3();
		TaskMng tm=TS_MC3.ts4();
		Anal a=new AnalFMC();
		a.init(tm);
		a.prepare();
		a.prn();
		return -1;
	}

	public int test2() 
	{
//		TaskMng tm=TS_MC1.ts6();
		TaskMng tm=TS_MC1.ts1();
		Anal a=new AnalEDF_VD();
		a.init(tm);
		a.prepare();
		a.prn();
		a=new AnalEDF_AD_E();
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
