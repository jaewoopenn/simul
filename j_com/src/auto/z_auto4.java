package auto;


// auto comp level 

import util.SEngineT;


public class z_auto4 {
	
	public static void init_s() {
//		int s=1;
		int s=2;
//		int s=3;
//		int s=4;
		
//		int log=1;
//		int log=2;
		int log=3;
		
		s_idx=s;
		s_log_level=log;
	}
	
	public void init() {
		g_num=2000;
//		g_num=100;
		g_cfg="_cfg.txt";
		g_ts="_ts.txt";
		g_xaxis="_x.txt";
		g_rs="_rs.txt";
		int anal[]= {0,1,2};
//		int anal[]= {2};
		g_anal=anal;
		
	}

	public void loop_util() {
		HPlatform p=new HPlatform(g_path);
		p.setNum(g_num);
		p.genCfg_util(65,95,5,g_cfg);
		p.genTS(g_cfg,g_ts,g_xaxis);
		p.setPeriod(g_period);
		p.anal_loop(g_rs,g_ts,g_anal);
		DataAnal da=new DataAnal(g_path,0);
		da.load_x(g_xaxis);
		da.load_rs(g_rs);
		da.save("_graph.txt");
		
	}

	public int test1()  {// config gen
		init();
		g_path="com/t2";
		loop_util();
		return 0;
	}
	public int test2() {// task set gen
		init();
		int p[]= {10,25,50};
		int st=0;
		for(int i=st;i<3;i++) {
			g_path="com/c"+i;
			g_period=p[i];
			loop_util();
		}
		return -1;	
	}
	
	public int test3() { // anal 
		return -1;
	}
	public  int test4() { // anal rs --> graph
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
	private static int s_idx;
	private static int s_log_level;
	private String g_path;
	private int g_num;
	private int[] g_anal;
	private String g_cfg;
	private String g_ts;
	private String g_xaxis;
	private String g_rs;
	private int g_period;
}
