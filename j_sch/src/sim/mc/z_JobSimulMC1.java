package sim.mc;

import sim.job.Job;
import util.SEngineT;
import util.SLog;
import util.SLogF;

public class z_JobSimulMC1 {
	public static int idx=3;
	public static int log_level=1;


	public int test1()	{
		JobSimulMC_indep js=new JobSimulMC_indep(3);
		js.add(new Job(0,5,2));
		js.add(new Job(1,6,1,2,2));
		js.add(new Job(2,7,1,3,2));
		js.simulBy(6);
		return -1;
	}
	public int test2() {
		JobSimulMC_indep js=new JobSimulMC_indep(0);
		js.setJM(z_ex.Job_MC1.ts1());
		js.simulBy(6);
		return 0;
	}
	
	
	public int test3() {
		SLogF.init("test.txt");
		JobSimulMC_indep js=new JobSimulMC_indep(3);
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
		Class c = z_JobSimulMC1.class;
		z_JobSimulMC1 m=new z_JobSimulMC1();
		int[] aret=z_JobSimulMC1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}