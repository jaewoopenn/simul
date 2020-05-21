package sim.job;

import util.SEngineT;
import util.SLog;
import util.SLogF;
import z_ex.Job_MC1;

public class z_JobSimul2 {
	public static int idx=4;
	public static int log_level=1;


	public int test1()	{
		JobSimul_indep js=new JobSimul_indep(0);
		js.setJM(Job_MC1.ts3());
//		js.setVisible(false);
		js.simul(6);
		return -1;
	}
	public int test2() {
		JobSimul_indep js=new JobSimul_indep(3);
		js.add(new Job(1,3,1));
		js.add(new Job(2,4,2));
		js.add(new Job(0,5,1));
		js.simulBy(6);
		js.add(new Job(0,8,1));
		js.simulBy(10);
		js.simul_end();
		return 0;
	}
	
	public  int test3()	{
		JobSimul_indep js=new JobSimul_indep(3);
		js.add(new Job(1,3,1));
		js.add(new Job(2,4,2));
		js.add(new Job(0,5,1));
		js.simul_one();
		js.simul_one();
		js.simul_one();
		js.simul_end();
		
		return 0;
	}
	
	
	public  int test4() {
		SLogF.init("test.txt");
		JobSimul_indep js=new JobSimul_indep(3);
		Job j=new Job(3,2,1);
		j.drop();
		js.add(j);
		js.add(new Job(1,3,1));
		js.add(new Job(2,3,1));
		js.add(new Job(0,2,2));
		for(int i=0;i<5;i++) {
			int dm=js.simul_one();
			SLog.prn(1, i+":"+dm);
		}
		SLogF.end();
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
		Class c = z_JobSimul2.class;
		z_JobSimul2 m=new z_JobSimul2();
		int[] aret=z_JobSimul2.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}