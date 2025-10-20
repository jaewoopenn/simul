package auto;


import anal.AutoAnal;
import anal.DoAnal;
import sim.AutoSimul;
import sim.DataSim_IMC;
import sim.DoSimul;
import util.MList;
import util.SEngineT;
import util.SLog;

// MC-RUN
// ADM
// TODO: dl miss 있음  ... 



public class z_auto_sim_imc {
	
	
	public static void init_s() {
		
		s_idx=1; // all together
//		s_idx=2;  // gen
//		s_idx=3;
//		s_idx=4; 
		
//		s_log_level=1;
		s_log_level=2;
	}
	
	public void init_g() {
		g_path="adm/sim";
		g_sort_max = 2; // 1: EDF-ADM, 2: EDF-VD-IMC
		
//		g_num=10;
//		g_num=50;
		g_num=500;
//		g_num=5000;
		
//		g_dur=1000;
		g_dur=10000;
//		g_dur=32000;

		// gen related;
//		g_stage=1;
//		g_stage=3;
//		g_stage=4;
		g_stage=5;
		g_upper=0.15;

		g_st=70;
		g_step=3;
		g_end=94;
		g_p_hc=0.5;
		g_ratio=-1;
		
	}


	
	public void init_sim() {
		
		//sim related;
		g_p_ms=0.3;
		g_rs="a_sim_list.txt";
		g_graph="a_sim_graph.txt";
		g_rs_path="adm/sim_rs";
		
		//misc
		g_cf="a_cfg_list.txt";
		g_ts="a_ts_list.txt";
		g_xl="a_x_list.txt";
		
	}
	public void gen() {
		AutoParConfig apg=AutoParConfig.init();
		apg.num=g_num;
		apg.p_hc=g_p_hc;
		apg.ratio=g_ratio;
		AutoConfig a=new AutoConfig(g_path,apg);
		a.genCfg_util(g_cf,g_st,g_step,g_end);
		AutoSysGen p=new AutoSysGen(g_path);
		p.setSch();
		p.setOnlyMC();
		p.setStage(g_stage);
		p.setUpper(g_upper);
		p.genTS(g_cf,g_ts);
		
		p.genXA(g_cf,g_rs_path+"/"+g_xl);
	}
	public void simul() {
		MList fu=MList.new_list();
		for(int i=0;i<g_sort_max;i++) {
			DoSimul ds=new DoSimul(i,g_dur,g_p_ms);
			AutoSimul as=new AutoSimul(g_path,ds);
			as.setRS(g_rs_path);
			String rs=as.simulList(g_ts);
			fu.add(rs);
		}
		fu.saveTo(g_rs_path+"/"+g_rs);
	}
	public void anal() {
		MList fu=MList.new_list();
		for(int i=0;i<g_sort_max;i++) {
			DoAnal da=new DoAnal(i);
			AutoAnal as=new AutoAnal(g_path,da);
			as.setRS(g_rs_path);
			as.setSimul();
			String rs=as.analList(g_ts);	
			fu.add(rs);
		}
		fu.saveTo(g_rs_path+"/"+g_rs);
	}	
	
	public void loop_util(int bQuick) {

		
		SLog.prn(2, "p:"+g_p_ms);
		if(bQuick==0)
			simul();
		else
			anal();
		
		
		DataSim_IMC ds=new DataSim_IMC(g_rs_path,0);
		ds.load_x(g_xl);
		ds.load_rs(g_rs);
		ds.saveSim(g_graph);
	}
	public int test1()  
	{
		init_g();
		init_sim();
		gen();
		loop_util(0);
		return 0;
	}
	public int test2() 
	{
		init_g();
		init_sim();
		gen();
		return 0;
	}
	public int test3() 
	{
		init_g();
		init_sim();
		loop_util(0);
		return 0;
	}
	public  int test4() // ratio
	{
		init_g();
		init_sim();
		gen();
		loop_util(1);
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
	private static int s_idx;
	private static int s_log_level;
	private String g_path;
	private double g_p_ms;
	private double g_p_hc;
	private double g_ratio;
	private double g_upper;
	private int g_st;
	private int g_step;
	private int g_stage;
	private int g_end;
	private int g_num;
	private int g_dur;
	private int g_sort_max;
	private String g_cf;
	private String g_ts;
	private String g_xl;
	private String g_rs;
	private String g_rs_path;
	private String g_graph;

}
