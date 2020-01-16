package auto;


import util.SEngineT;


public class z_auto2 {
	private static int s_idx;
	private static int s_log_level;
	private String g_path;
	private double g_prob;
	private double g_util_ul;
	private int g_num;
	private String g_cf;
	private String g_ts;
	private String g_xl;
	private String g_rs;
	private String g_graph;
	
	public static void init_s() {
//		int s=1;
		int s=2;
//		int s=3;
		
		s_idx=s;
		
		s_log_level=1;
	}
	
	public void init_g() {
		g_path="sch/t1";
		g_num=100;
		g_cf="a_cfg_list.txt";
		g_ts="a_ts_list.txt";
		g_xl="a_x_list.txt";
		
	}

	public void init_anal() {
		g_util_ul=1.0;
		g_rs="a_rs_list.txt";
		g_graph="a_graph.txt";
	}

	public void init_sim() {
		g_prob=0.3;
		g_util_ul=0.95;
		g_rs="a_sim_list.txt";
		g_graph="a_sim_graph.txt";
	}
	public void main_loop() {
		Platform p=new Platform(g_path);
		p.setNum(g_num);
		p.genCfg_util(g_cf,g_util_ul);
		p.setNum(g_num);
		p.genCfg_util(g_cf,g_util_ul);
		p.setCheck();
		p.genTS(g_cf,g_ts,g_xl);
		p.setProb(g_prob);
		p.sim_loop(g_rs, g_ts, 2);
		DataSim ds=new DataSim(g_path,0);
		ds.load_x(g_xl);
		ds.load_rs(g_rs);
		ds.saveSim(g_graph);
	}
	public int test1() 
	{
		init_g();
		init_sim();
		g_path="sch/t2";
		main_loop();
		return 0;
	}
	public int test2() // from 
	{
		init_g();
		init_sim();
		int a[]= {1,3,5};
		for(int i=0;i<3;i++) {
			g_path="sch/p"+i;
			g_prob=0.1*a[i];
			main_loop();
		}
		return 0;
	}
	public int test3() // task set --> anal rs
	{
		return 0;
	}
	public  int test4() // anal rs --> graph
	{
		return 0;
	}
	public  int test5() // gen TS w/ schedulable
	{
		return 0;
	}
	public  int test6() // ts --> simul rs
	{
		return 0;
	}
	public  int test7()// simul rs --> graph
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
		z_auto2.init_s();
		Class c = z_auto2.class;
		z_auto2 m=new z_auto2();
		int[] aret=z_auto2.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
