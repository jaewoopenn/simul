package auto;


import util.SEngineT;
//MC-FLEX p hc ratio

public class z_auto6 {
	private static int s_idx;
	private static int s_log_level;
	private String g_path;
	private double g_p_ms;
	private int g_dur;
	
	public static void init_s() {
//		int s=1;
//		int s=2; //p
//		int s=3; //hc
//		int s=4; //ratio
		int s=5; 
		
		s_idx=s;
		
		s_log_level=1;
	}
	
	public void init_g() {
		g_path="sch/t1";
		g_dur=30000;
	}


	public void init_sim() {
		g_p_ms=0.3;
	}
	public int test1() 
	{
		return 0;
	}
	public int test2() // p
	{
		return 0;
	}
	public int test3() // hc
	{
		return 0;
	}
	public  int test4() // ratio
	{
		return 0;		
	}
	public  int test5() 
	{
		init_g();
		init_sim();
		g_path="sch/p2";
		g_p_ms=0.5;
		int st=55;
		int item=2;
		for(int i=st;i<100;i+=5) {
			Platform p=new Platform(g_path);
			p.setP_MS(g_p_ms);
			p.setDur(g_dur);
			p.simul_a("sch/p2/taskset_"+i, item);
		}
		return 0;
	}
	public  int test6() 
	{
		return 0;
	}
	public  int test7()
	{
		return 0;
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
		z_auto6.init_s();
		Class c = z_auto6.class;
		z_auto6 m=new z_auto6();
		int[] aret=z_auto6.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
