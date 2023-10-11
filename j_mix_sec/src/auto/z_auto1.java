package auto;


import util.SEngineT;


public class z_auto1 {
	private static int s_idx;
	private static int s_log_level;
	private String g_path;
	private int g_ts_num;
	private int g_anal_num;
	private String g_cf;
	private String g_ts;
	private String g_xl;
	private String g_rs;
	private String g_graph;
	
	public static void init_s() {
		int s=1;
		s_idx=s;
		
		s_log_level=2;
	}
	
	public void init_g() {
		g_path="mix_sec/t1";
		g_ts_num=5000;
//		g_num=100;
//		g_num=5;
		g_cf="a_cfg_list.txt";
		g_ts="a_ts_list.txt";
		g_xl="a_x_list.txt";
		g_anal_num=4;
		
	}

	public void init_anal() {
		g_rs="a_rs_list.txt";
		g_graph="a_graph.txt";
	}

	public int test1() 
	{
		init_g();
		init_anal();
		Platform p=new Platform(g_path);
		p.setNum(g_ts_num);
		p.genCfg_util(g_cf,20,5,95);
		p.genTS(g_cf,g_ts);
		p.genXA(g_cf,g_xl);
		p.anal_loop(g_rs,g_ts,g_anal_num);
		DataAnal da=new DataAnal(g_path,0);
		da.load_x(g_xl);
		da.load_rs(g_rs);
		da.save(g_graph);
		return 0;
	}
	public int test2() // from 
	{
		init_g();
		init_anal();
		DataAnal da=new DataAnal(g_path,0);
		da.load_x(g_xl);
		da.load_rs(g_rs);
		da.save(g_graph);
		return -1;	
	}
	public int test3() // task set --> anal rs
	{
		init_g();
		init_anal();
		Platform p=new Platform(g_path);
		p.setNum(2);
		p.anal_loop(g_rs,g_ts,g_anal_num);
		return -1;
	}
	public  int test4() // anal rs --> graph
	{
		return -1;
	}
	public  int test5() // gen TS w/ schedulable
	{
		return 0;
	}
	public  int test6() // ts --> simul rs --->graph
	{
		return -1;
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

}
