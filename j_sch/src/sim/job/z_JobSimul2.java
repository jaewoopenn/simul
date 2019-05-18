package sim.job;

import comp.z_Anal1;
import util.SEngineT;
import z_ex.Job_MC1;

public class z_JobSimul2 {
	public static int idx=3;
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