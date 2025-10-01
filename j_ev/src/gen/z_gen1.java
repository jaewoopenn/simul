package gen;



import util.SEngineT;

public class z_gen1 {
	public static void init_s() {
//		s_idx=1;
		s_idx=2;
//		s_idx=3;
//		s_idx=4;
//		s_idx=5;
		
		
		s_log_level=1;
	}

	public int test1() 
	{
		JGen jg=JGen.init();
		jg.run();
		jg.save("ev/test2.txt");
		return -1;
	}

	public int test2() {
		JRun jr=JRun.init("ev/test2.txt");
		jr.start();
		return -1;
	}
	public int test3() {
		return -1;
	}
	public  int test4() {
		return 1;
	}
	public  int test5() {
		return -1;
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
		z_gen1.init_s();
		Class c = z_gen1.class;
		z_gen1 m=new z_gen1();
		int[] aret=z_gen1.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	private static int s_idx;
	private static int s_log_level;

}
