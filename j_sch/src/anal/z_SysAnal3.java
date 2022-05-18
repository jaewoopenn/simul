package anal;

import z_ex.TS_MC2;
import z_ex.TS_MC3;
import z_ex.TS_MC5;
import imc.AnalEDF_RUN;
import task.TaskMng;
import util.SEngineT;

public class z_SysAnal3 {
//	public static int idx=1;
	public static int idx=4;
	public static int log_level=1;

	public int test1() 
	{
//		TaskMng tm=TS_MC2.ts1();
//		TaskMng tm=TS_MC5.ts1();
//		TaskMng tm=TS_MC5.ts2();
//		TaskMng tm=TS_MC5.ts3();
		TaskMng tm=TS_MC5.ts4();
//		TaskMng tm=TS_MC5.ts5();
		Anal a=new AnalEDF_IV4();
//		Anal a=new AnalEDF_IV2();
		a.init(tm);
		a.prepare();
		a.prn();
		
		return -1;
	}

	public int test2() {  // 문제 발생.... HC 태스크가 하나일 경우, 처리 못함 ... d값 하한 설정 필요. 
		TaskMng tm=TS_MC3.ts1();
		Anal a=new AnalEDF_IV2();
		a.init(tm);
		a.prepare();
		a.prn();
		return -1;
	}
	public int test3() {
		TaskMng tm=TS_MC2.ts2();
		Anal a=new AnalEDF_IV2();
		a.init(tm);
		a.prepare();
		a.prn();
		return -1;
	}
	public  int test4() {
		TaskMng tm=TS_MC2.ts2();
		Anal a=new AnalEDF_RUN();
		a.init(tm);
		a.prepare();
		a.prn();
		return -1;
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
		Class c = z_SysAnal3.class;
		z_SysAnal3 m=new z_SysAnal3();
		int[] aret=z_SysAnal3.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
