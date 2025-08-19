package autorun;


import auto.AutoConfig;
import auto.AutoParConfig;
import auto.AutoSimul;
import auto.DataSim_IMC;
import auto.AutoTaskGen;
import sim.DoSimul;
import util.MList;
import util.SEngineT;
import util.SLog;

// MC-RUN
// ADM

public class z_auto_sim_imc4 {
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
		int s=1; // ADM simul 
//		int s=2; //
//		int s=3; //
//		int s=4; //
//		int s=5; //
		
		s_idx=s;
		
//		s_log_level=1;
		s_log_level=2;
	}
	
	public void init_g() {
		g_path="adm/sim";
		g_num=100;
//		g_num=500;
//		g_num=5000;
		
		g_dur=10000;
//		g_dur=32000;
		g_cf="a_cfg_list.txt";
		g_ts="a_ts_list.txt";
		g_xl="a_x_list.txt";
		
	}


	public void init_sim() {
		g_p_ms=0.3;
		g_p_hc=0.5;
		g_ratio=-1;
		g_st=72;
		g_step=3;
		g_end=96;
		g_rs="a_sim_list.txt";
		g_graph="a_sim_graph.txt";
	}
	public void gen() {
		AutoParConfig apg=new AutoParConfig();
		apg.num=g_num;
		apg.p_hc=g_p_hc;
		apg.ratio=g_ratio;
		AutoConfig a=new AutoConfig(g_path,apg);
		a.genCfg_util(g_cf,g_st,g_step,g_end);
		AutoTaskGen p=new AutoTaskGen(g_path);
		p.setRS(g_path);
		p.setSch();
		p.setOnlyMC();
		p.genTS(g_cf,g_ts);
		
	}
	public void simul(String rs_dir) {
		MList fu=new MList();
		for(int i=0;i<2;i++) {
			DoSimul ds=new DoSimul(i);
			ds.setProb(g_p_ms);
			ds.setDur(g_dur);
			AutoSimul as=new AutoSimul(g_path,ds);
			as.setRS(rs_dir);
			String rs=as.simulList(g_ts);
			fu.add(rs);
		}
		fu.saveTo(rs_dir+"/"+g_rs);
	}
	
	public void loop_util(String rs_path) {
		String rs_dir=rs_path;
		AutoTaskGen p=new AutoTaskGen(g_path);
		p.setRS(rs_path);

		p.genXA(g_cf,g_xl);
		
		SLog.prn(2, "p:"+g_p_ms);
		simul(rs_dir);
		
		
		DataSim_IMC ds=new DataSim_IMC(rs_dir,0);
		ds.load_x(g_xl);
		ds.load_rs(g_rs);
		ds.saveSim(g_graph);
	}
	public int test1() 
	{
		init_g();
		init_sim();
		gen();
		String rs_path="adm/sim_rs";
		loop_util(rs_path);
		return 0;
	}
	public int test2() // p
	{
		return 0;
	}
	public int test3() // ms length
	{
		return 0;
	}
	public  int test4() // ratio
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
		z_auto_sim_imc4.init_s();
		Class c = z_auto_sim_imc4.class;
		z_auto_sim_imc4 m=new z_auto_sim_imc4();
		int[] aret=z_auto_sim_imc4.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
