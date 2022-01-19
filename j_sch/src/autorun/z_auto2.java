package autorun;


import auto.DataSim;
import auto.Platform;
import util.SEngineT;
import util.SLog;
//MC-FLEX p hc ratio (BRE)

public class z_auto2 {
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
//		int s=1;
		int s=2; //p
//		int s=3; //hc
//		int s=4; //ratio
//		int s=5; //all in one.
		
		s_idx=s;
		
		s_log_level=2;
	}
	
	public void init_g() {
		g_path="ind/t1";
//		g_num=5000;
//		g_dur=32000;
		g_num=500;
		g_dur=10000;
		g_cf="a_cfg_list.txt";
		g_ts="a_ts_list.txt";
		g_xl="a_x_list.txt";
		
	}


	public void init_sim() {
		g_p_ms=0.2;
		g_p_hc=0.5;
		g_ratio=-1;
		g_util_ul=0.95;
		g_rs="a_sim_list.txt";
		g_graph="a_sim_graph.txt";
	}
	public void loop_util() {
		Platform p=new Platform(g_path);
		p.setNum(g_num);
		p.setP_HC(g_p_hc);
		p.setRatio(g_ratio);
		p.genCfg_util(g_cf,g_util_ul);
		p.setCheck();
		p.genTS(g_cf,g_ts,g_xl);
		p.setP_MS(g_p_ms);
		SLog.prn(2, "p:"+g_p_ms);
		p.setDur(g_dur);

//.......................................  DRE (comment out), BRE (setBE)  best effort 하지 말자 
//		p.setBE();
		
//		p.sim_loop(g_rs, g_ts,0,5);
		p.sim_loop(g_rs, g_ts,0,3);
//		p.sim_loop(g_rs, g_ts,0,2);
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
		loop_util();
		return 0;
	}
	public int test2() // p
	{
		int st=0,et=3;
//		int st=1,et=2;
//		int st=2,et=3;
		init_g();
		init_sim();
		double a[]= {0.05,0.2,0.5};
//		double a[]= {0.2,0.5,0.7};
		for(int i=st;i<et;i++) {
			g_path="ind/p"+i;
			g_p_ms=a[i];
			loop_util();
		}
		return 0;
	}
	public int test3() // hc
	{
		init_g();
		init_sim();
		double a[]= {0.25,0.5,0.75};
		int st=0;
		for(int i=st;i<3;i++) {
			g_path="sch/h"+i;
			g_p_hc=a[i];
			loop_util();
		}
		return 0;
	}
	public  int test4() // ratio
	{
		init_g();
		init_sim();
		double a[]= {0.2,0.4,0.6};
		int st=0;
		for(int i=st;i<3;i++) {
			g_path="sch/r"+i;
			g_ratio=a[i];
			loop_util();
		}
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
