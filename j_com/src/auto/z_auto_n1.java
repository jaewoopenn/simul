package auto;


// auto task level ... varying tu

import util.SEngineT;


public class z_auto_n1 {
	
	public static void init_s() {
		int s=1;
//		int s=2;
//		int s=3;
//		int s=4;
		
//		int log=1;
//		int log=2;
		int log=3;
		
		s_idx=s;
		s_log_level=log;
	}
	
	public void init() {
//		g_num=25;
//		g_num=50;
		g_num=100;
//		g_num=1000;
//		g_num=3000;
		g_cfg="_cfg.txt";
		g_ts="_ts.txt";
		g_xaxis="_x.txt";
		g_rs="_rs.txt";
		int anal[]= {0,1,2};
//		int anal[]= {2};
		g_anal=anal;
		
	}


	public int test1()  {  // varying tu 
		init();
//		double lb[]= {-1,-1,-2,-3};
		double lb[]= {0.002,-1,-2,-3};
		double ub[]= {0.1,0.04,0.06,0.1};
		int st=0;
		for(int i=st;i<1;i++) {
//		for(int i=st;i<4;i++) {
			g_path="com/u"+i;
			g_t_lb=lb[i];
			g_t_ub=ub[i];
			loop_util();
		}
		
		return 0;
	}
	public int test2() {
		return -1;	
	}
	
	public int test3() { 
		return -1;
	}
	public  int test4() {
		init();
		return -1;
	}
	public  int test5() {
		return -1;
	}
	public  int test6() {
		return -1;
	}
	public  int test7() {
		return -1;
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

	public void loop_util() {
		Platform p=new Platform(g_path);
		p.setTU(g_t_lb,g_t_ub);
		p.setNum(g_num);
		p.genCfg_util(30,70,5,g_cfg);
		p.genTS(g_cfg,g_ts,g_xaxis);
		p.anal_loop(g_rs,g_ts,g_anal);
		DataAnal da=new DataAnal(g_path,0);
		da.load_x(g_xaxis);
		da.load_rs(g_rs);
		da.save("_graph.txt");
		da=new DataAnal(g_path,1);
		da.load_x(g_xaxis);
		da.load_rs(g_rs);
		da.save("_graph2.txt");		
		
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		z_auto_n1.init_s();
		Class c = z_auto_n1.class;
		z_auto_n1 m=new z_auto_n1();
		int[] aret=z_auto_n1.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	private static int s_idx;
	private static int s_log_level;
	private String g_path;
	private int g_num;
	private int[] g_anal;
	private String g_cfg;
	private String g_ts;
	private String g_xaxis;
	private String g_rs;
	private double g_t_ub;
	private double g_t_lb;
}
