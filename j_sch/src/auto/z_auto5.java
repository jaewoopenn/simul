package auto;


import util.SEngineT;
// compare task drop method (impl: refer auto4 with dur) 
// item 1: minTaskDrop , item2: minJobDrop
// x축은 util이고, y축은 DMR 


public class z_auto5 {
	private static int s_idx;
	private static int s_log_level;
	private String g_path;
	private double g_p_ms;
	private double g_p_hc;
	private double g_ratio;
	private double g_util_ul;
	private int g_num;
	private int g_dur;
	private String g_cf;
	private String g_ts;
	private String g_xl;
	private String g_rs;
	private String g_graph;
	
	public static void init_s() {
		int s=1;
//		int s=2;
//		int s=3;
		
		s_idx=s;
		
		s_log_level=1;
	}
	
	public void init_g() {
		g_path="sch/t1";
		g_num=500;
		g_dur=5000;
		g_cf="a_cfg_list.txt";
		g_ts="a_ts_list.txt";
		g_xl="a_x_list.txt";
		
	}

	public void init_sim() {
		g_p_ms=0.3;
		g_p_hc=0.5;
		g_ratio=-1;
		g_util_ul=0.95;
		g_rs="a_sim_list.txt";
		g_graph="a_sim_graph.txt";
	}
	public void loop_other() {
		Platform p=new Platform(g_path);
		p.setNum(g_num);
		p.genCfg_util(g_cf,g_util_ul);
		p.setCheck();
		p.genTS(g_cf,g_ts,g_xl);
		p.setP_MS(g_p_ms);
		p.setP_HC(g_p_hc);
		p.setRatio(g_ratio);
		p.setDur(g_dur);
		p.sim_loop(g_rs, g_ts,3,4);
		DataSim ds=new DataSim(g_path,0);
		ds.load_x(g_xl);
		ds.load_rs(g_rs);
		ds.saveSim(g_graph);
	}
	public int test1() 
	{
		init_g();
		init_sim();
		loop_other();
		return 0;
	}
	public int test2() // hc
	{
		return 0;
	}
	public int test3() // ratio
	{
		return 0;
	}
	public  int test4() // hc
	{
		return 0;
	}
	public  int test5() // ratio
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
		z_auto5.init_s();
		Class c = z_auto5.class;
		z_auto5 m=new z_auto5();
		int[] aret=z_auto5.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
