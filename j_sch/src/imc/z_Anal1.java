package imc;

import z_ex.TS_IMC1;
import z_ex.TS_IMC2;
import anal.Anal;
import task.TaskMng;
import util.SEngineT;
import util.SLog;

public class z_Anal1 {
//	public static int idx=1;
//	public static int idx=2;
	public static int idx=3;
	public static int log_level=1;

	public int test1() 
	{
		TaskMng tm=TS_IMC1.ts1();
//		SLog.prn(1, tm.getLoUtil());
//		SLog.prn(1, tm.getDeLoUtil());
		Anal a=new AnalEDF_IMC();
		a.init(tm);
		a.prn();
		
		a=new AnalEDF_VD_IMC();
		a.init(tm);
		a.prn();
		return -1;
	}

	public int test2() 
	{
		TaskMng tm=TS_IMC1.ts2();
		Anal a=new AnalEDF_VD_IMC();
		a.init(tm);
		a.prn();
		return -1;
	}
	

	public int test3() 
	{
		int no=1;
//		int no=2;
//		int no=3;
//		int no=4;
//		int no=5;
//		int no=6;
//		int no=7;
		
		
		
		TaskMng tm=TS_IMC2.getTS(no);
		tm.prnTxt();
		tm.prnOffline();
		Anal a;

		a=new AnalEDF_RUN();
		a.init(tm);
		double d=a.getDtm();
		SLog.prn(1, "det:"+d);
		
		a.prn();
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
		Class c = z_Anal1.class;
		z_Anal1 m=new z_Anal1();
		int[] aret=z_Anal1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
