package autorun;


import util.SEngineT;
import util.SLog;

// IMC-PnG 
// revision / MO ratio 

public class z_auto_sim_imc2 {
	private static int s_idx;
	private static int s_log_level;
	private String g_path;
	private double g_p_ms;
	private double g_p_hc;
	private double g_ratio;
	private int g_num;
	private int g_life;
	private int g_dur;
	private String g_cf;
	private String g_ts;
	private String g_xl;
	private String g_rs;
	private String g_graph;
	
	
	public static void init_s() {
//		int s=1;
//		int s=2; //mo
		int s=3; //hc
		
		s_idx=s;
		
		s_log_level=2;
	}
	
	public void init_g() {
//		g_num=100;
//		g_num=500;
		g_num=5000;
		
//		g_dur=10000;
		g_dur=32000;
		g_cf="a_cfg_list.txt";
		g_ts="a_ts_list.txt";
		g_xl="a_x_list.txt";
		
	}


	public void init_sim() {
		g_p_ms=0.1;
		g_life=200;
//		g_life=0;
		g_p_hc=0.5;
		g_ratio=-1;
		g_rs="a_sim_list.txt";
		g_graph="a_sim_graph.txt";
	}
	public void gen() {
		Platform_IMC p=new Platform_IMC(g_path,g_path);
		p.setNum(g_num);
		p.setP_HC(g_p_hc);
		p.setRatio(g_ratio);
		p.genCfg_mo(g_cf,0,10,100);
		p.setCheck();
		//p.setOnlyMC();
		p.genTS(g_cf,g_ts);
	}
	public void gen2() {
		Platform_IMC p=new Platform_IMC(g_path,g_path);
		p.setNum(g_num);
		p.setRatio(g_ratio);
		p.genCfg_hc(g_cf,0,10,100);
		p.setCheck();
		//p.setOnlyMC();
		p.genTS(g_cf,g_ts);
	}
	
	public void loop_util(String rs_path) {
		Platform_IMC p=new Platform_IMC(g_path,rs_path);

		p.genXA(g_cf,g_xl);
		
		p.setP_MS(g_p_ms);
		SLog.prn(2, "p:"+g_p_ms);
		p.setDur(g_dur);
		p.setLife(g_life);
		
		p.sim_loop(g_rs, g_ts,0,3);
		DataSim_IMC ds=new DataSim_IMC(rs_path,0);
		ds.load_x(g_xl);
		ds.load_rs(g_rs);
		ds.saveSim(g_graph);
	}
	public int test1() 
	{
//		init_g();
//		init_sim();
//		g_path="sch/t2";
//		loop_util();
		return 0;
	}
	public int test2() // p
	{
		init_g();
		init_sim();
		g_path="run/mo_ts";
		gen();
		String rs_path="run/mo";
		loop_util(rs_path);
		return 0;
	}
	public int test3() // 
	{
		init_g();
		init_sim();
		g_path="run/hc_ts";
		gen2();
		String rs_path="run/hc";
		loop_util(rs_path);
		return 0;
	}
	public  int test4() // 
	{
		return 0;
	}
	public  int test5() 
	{
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
		z_auto_sim_imc2.init_s();
		Class c = z_auto_sim_imc2.class;
		z_auto_sim_imc2 m=new z_auto_sim_imc2();
		int[] aret=z_auto_sim_imc2.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
