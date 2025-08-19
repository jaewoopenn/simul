package autorun;


import auto.DataSim_IMC;
import auto.Platform_IMC;
import auto.Platform_base;
import util.SEngineT;
import util.SLog;

// MC-RUN
// ADM

public class z_auto_sim_imc {
	private static int s_idx;
	private static int s_log_level;
	private String g_path;
	private double g_p_ms;
	private double g_p_hc;
	private double g_ratio;
	private int g_st;
	private int g_step;
	private int g_end;
	private int g_num;
	private int g_dur;
	private String g_cf;
	private String g_ts;
	private String g_xl;
	private String g_rs;
	private String g_graph;
	
	
	public static void init_s() {
//		int s=1; // ADM simul 
		int s=2; //
//		int s=3; //
//		int s=4; //
//		int s=5; //
		
		s_idx=s;
		
//		s_log_level=1;
		s_log_level=2;
	}
	
	public void init_g() {
		g_path="adm/test1";
		g_num=10;
//		g_num=500;
//		g_num=5000;
		
		g_dur=10000;
//		g_dur=32000;
		g_cf="a_cfg_list.txt";
		g_ts="a_ts_list.txt";
		g_xl="a_x_list.txt";
		
	}


	public void init_sim() {
		g_p_ms=0.5;
//		g_life=0;
		g_p_hc=0.5;
		g_ratio=-1;
		g_st=72;
		g_step=3;
		g_end=96;
		g_rs="a_sim_list.txt";
		g_graph="a_sim_graph.txt";
	}
	public void gen() {
		Platform_base p=new Platform_base(g_path);
		p.setNum(g_num);
		p.setP_HC(g_p_hc);
		p.setRatio(g_ratio);
		p.genCfg_util(g_cf,g_st,g_step,g_end);
		p.setSch();
		p.setOnlyMC();
		p.genTS(g_cf,g_ts);
		
	}
	public void loop_util(String rs_path) {
		Platform_base p=new Platform_base(g_path);
		p.setRS(rs_path);

		p.genXA(g_cf,g_xl);
		
		p.setP_MS(g_p_ms);
		SLog.prn(2, "p:"+g_p_ms);
		p.setDur(g_dur);
		
//		p.sim_loop(g_rs, g_ts,0,2);
//		DataSim_IMC ds=new DataSim_IMC(rs_path,0);
//		ds.load_x(g_xl);
//		ds.load_rs(g_rs);
//		ds.saveSim(g_graph);
	}
	public int test1() 
	{
		init_g();
		init_sim();
		gen();
		String rs_path="adm/pi0";
		loop_util(rs_path);
		return 0;
	}
	public int test2() // p
	{
		init_g();
		init_sim();
//		gen();
		String rs_path="adm/pi0";
		loop_util(rs_path);
		return 0;
	}
	public int test3() // ms length
	{
		int st=0,et=3;
//		int st=1,et=2;
//		int st=2,et=3;
		init_g();
		init_sim();
//		double a[]= {0.05,0.1,0.15};
		double a[]= {0.2,0.5,0.7};
		g_path="adm/test1";
		gen();
		for(int i=st;i<et;i++) {
			String rs_path="adm/pi"+i;
			g_p_ms=a[i];
			loop_util(rs_path);
		}
		return 0;
	}
	public  int test4() // ratio
	{
//		init_g();
//		init_sim();
//		double a[]= {0.2,0.4,0.6};
//		int st=0;
//		for(int i=st;i<3;i++) {
//			g_path="sch/r"+i;
//			g_ratio=a[i];
//			loop_util();
//		}
		return 0;		
	}
	public  int test5() 
	{
		test2();
		test3();
		test4();
		return 0;
	}
	public  int test6() 
	{
//		init_g();
//		init_sim();
//		g_path="sch/t2";
//		loop_util();
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
		z_auto_sim_imc.init_s();
		Class c = z_auto_sim_imc.class;
		z_auto_sim_imc m=new z_auto_sim_imc();
		int[] aret=z_auto_sim_imc.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
