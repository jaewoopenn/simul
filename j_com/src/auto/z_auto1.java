package auto;


import util.SEngineT;


public class z_auto1 {
	private static int s_idx;
	private static int s_log_level;
	
	public static void init_s() {
//		int s=1;
		int s=2;
//		int s=3;
//		int s=4;
		
		int log=1;
//		int log=2;
		
		s_idx=s;
		s_log_level=log;
	}
	
	public void init() {
		g_path="com/t1";
		g_num=100;
		g_cfg_list="_cfg_list.txt";
		g_ts="_ts_list.txt";
		g_xl="_x_list.txt";
		g_util_ul=1.0;
		g_rs="_rs_list.txt";
		g_graph="_graph.txt";
		g_anal_end=1;
		
	}


	public int test1()  {// config gen
		init();
		Platform p=new Platform(g_path);
		p.setNum(g_num);
		p.genCfg_util(g_util_ul,g_cfg_list);
		return 0;
	}
	public int test2() {// task set gen
		init();
		Platform p=new Platform(g_path);
		p.genTS(g_cfg_list,g_ts,g_xl);
		return -1;	
	}
	
	public int test3() { // anal 
		init();
		Platform p=new Platform(g_path);
		p.anal_loop(g_rs,g_ts,g_anal_end);
		return -1;
	}
	public  int test4() { // anal rs --> graph
		init();
		DataAnal da=new DataAnal(g_path,0);
		da.load_x(g_xl);
		da.load_rs(g_rs);
		da.save(g_graph);		
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
		z_auto1.init_s();
		Class c = z_auto1.class;
		z_auto1 m=new z_auto1();
		int[] aret=z_auto1.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	private String g_path;
	private double g_util_ul;
	private int g_num;
	private int g_anal_end;
	private String g_cfg_list;
	private String g_ts;
	private String g_xl;
	private String g_rs;
	private String g_graph;

}
