package auto;


import util.SEngineT;
// dur

public class z_auto4 {
	private static int s_idx;
	private static int s_log_level;
	private String g_path;
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
//		int s=4;
		
		s_idx=s;
		
		s_log_level=1;
	}
	
	public void init_g() {
		g_path="sch/dur";
		g_num=5000;
//		g_num=300;
		g_dur=4000;
		g_cf="a_cfg_list.txt";
		g_ts="a_ts_list.txt";
		g_xl="a_x_list.txt";
		
	}

	public void init_sim() {
		g_rs="a_sim_list.txt";
		g_graph="a_sim_graph.txt";
	}
	public void loop_util() {
		Platform p=new Platform(g_path);
		p.setNum(g_num);
		p.setP_HC(0.5);
		p.genCfg_util_one(g_cf,0.85); //0.9
		p.setCheck();
		p.genTS(g_cf,g_ts,g_xl);
		p.genXA(g_xl);
		p.setDur(g_dur);
		p.setBE();
		p.setP_MS(0.3);
		p.sim_loop_dur(g_rs, g_ts, 4);
		DataSim ds=new DataSim(g_path,0);
		ds.load_x(g_xl);
		ds.load_rs(g_rs);
		ds.saveSim(g_graph);
	}
	public int test1() 
	{
		init_g();
		init_sim();
		loop_util();
		return 0;
	}
	public int test2() // ms
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
		z_auto4.init_s();
		Class c = z_auto4.class;
		z_auto4 m=new z_auto4();
		int[] aret=z_auto4.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
