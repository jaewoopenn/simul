package auto;


import util.S_TEngine;


public class z_auto1 {
	private static int s_idx;
	private static int s_log_level;
	private String g_path;
	@SuppressWarnings("unused")
	private double g_prob;
	private double g_util_ul;
	private int g_num;
	private String g_cf;
	private String g_ts;
	private String g_xl;
	private String g_rs;
	@SuppressWarnings("unused")
	private String g_graph;
	
	public static void init_s() {
//		int s=1;
//		int s=2;
//		int s=3;
//		int s=4;
		
//		int s=1;
//		int s=5;
//		int s=6;
		int s=7;
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
	public int test1() 
	{
		init_g();
		init_anal();
		Platform p=new Platform(g_path);
		p.setNum(g_num);
		p.genCfg_util(g_cf,g_util_ul);
		return 0;
	}
	public int test2() // from 
	{
		init_g();
		init_anal();
		Platform p=new Platform(g_path);
		p.genTS(g_cf,g_ts,g_xl);
		return -1;	}
	public int test3() // task set --> anal rs
	{
		init_g();
		init_anal();
		Platform p=new Platform(g_path);
		int end=3;
		p.anal_loop(g_rs,g_ts,end);
		return -1;
	}
	public  int test4() // anal rs --> graph
	{
		return -1;
	}
	public  int test5() // gen TS w/ schedulable
	{
		return -1;
	}
	public  int test6() // ts --> simul rs
	{
		return -1;
	}
	public  int test7()// simul rs --> graph
	{
		return -1;
	}
	public  int test8()
	{
		return 0;
	}
	public  int test9()
	{
		return 0;
	}
	public  int test10()
	{
		return 0;
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		z_auto1.init_s();
		Class c = z_auto1.class;
		z_auto1 m=new z_auto1();
		int[] aret=z_auto1.gret;
		if(s_idx==-1)
			S_TEngine.run(m,c,aret,10);
		else
			S_TEngine.runOnce(m,c,aret,s_idx,s_log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
