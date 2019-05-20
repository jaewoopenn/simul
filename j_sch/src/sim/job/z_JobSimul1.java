package sim.job;

import util.SLog;
import util.SEngineT;

public class z_JobSimul1 {
	public static int idx=1;
	public static int log_level=1;


	public int test1()	{
		JobSimul_indep js=new JobSimul_indep(3);
		js.add(new Job(0,5,1));
		js.add(new Job(1,3,1));
		js.add(new Job(2,4,2));
		js.simulBy(3);
		js.add(new Job(1,6,1));
		SLog.prn(1,"  ");
		js.simulBy(4);
		js.add(new Job(2,8,2));
		js.simul(8);
		return -1;
	}
	public int test2() {
		JobSimul_indep js=new JobSimul_indep(3);
		js.add(new Job(0,3,2));
		js.add(new Job(1,3,2));
		js.add(new Job(2,3,2));
		js.simulBy(6);
		js.simul_end();
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
		Class c = z_JobSimul1.class;
		z_JobSimul1 m=new z_JobSimul1();
		int[] aret=z_JobSimul1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}