package task;

import sample.TS1;
import util.MCal;
import util.MList;
import util.SEngineT;
import util.SLog;

public class z_TaskSet1 {
	public static int idx=1;
	public static int log_level=1;
	public int test1()
	{
		TaskSet tm=TS1.tm2();
		int p[]=tm.getPeriods();
		for(int n:p) {
			SLog.prn(1,"p:"+n);
		}
		long lcm=MCal.lcm(p);
		SLog.prn(1,"lcm:"+lcm);
				
		return 1;
	}
	public int test2()
	{
		return 0;
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
		Class c = z_TaskSet1.class;
		z_TaskSet1 m=new z_TaskSet1();
		int[] aret=z_TaskSet1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};

}
