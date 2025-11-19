package job;


import util.SEngineT;

public class z_job1 {
	public static void init_s() {
		s_idx=1;
//		s_idx=2;
//		s_idx=3;
		
		
		s_log_level=1;
	}

	public int test1() 
	{
		int t=0;
		JobMng jm=new JobMng_DEM();
		JobSimul js=new JobSimul(jm);
		Job j=new Job(1,6,2);
		jm.add(j);
		j=new Job(2,5,2);
		jm.add(j);
		js.simul_one(t);
		t++;
		j=new Job(3,4,1);
		jm.add(j);
		js.simul_one(t);
		t++;
		js.simul_one(t);
		t++;
		
		return -1;
	}

	public int test2() {
		return -1;
	}
	public int test3() {
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
		z_job1.init_s();
		Class c = z_job1.class;
		z_job1 m=new z_job1();
		int[] aret=z_job1.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	private static int s_idx;
	private static int s_log_level;

}
