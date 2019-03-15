package auto;


import util.S_TEngine;


public class z_auto1 {
	private static int g_idx;
	private static int g_log_level;
	private static String g_path;
	
	public static void init() {
//		g_idx=1;
//		g_idx=2;
//		g_idx=3;
//		g_idx=4;
//		g_idx=5;
//		g_idx=6;
		g_idx=7;
		
		g_log_level=1;
		g_path="sch/t1";
	}
	
	public int test1() 
	{
		String cf="a_cfg_list.txt";
		Platform p=new Platform(g_path);
		p.setNum(100);
		p.genCfg_util(cf,100);
		return 0;
	}
	public int test2() // from 
	{
		String cf="a_cfg_list.txt";
		String ts="a_ts_list.txt";
		String xl="a_x_list.txt";
		Platform p=new Platform(g_path);
		p.genTS(cf,ts,xl);
		return -1;	}
	public int test3() // task set --> anal rs
	{
		String ts="a_ts_list.txt";
		String rs="a_rs_list.txt";
		Platform p=new Platform(g_path);
		int end=3;
		p.anal_loop(rs,ts,end);
		return -1;
	}
	public  int test4() // anal rs --> graph
	{
		String xl="a_x_list.txt";
		String rs="a_rs_list.txt";
		DataAnal da=new DataAnal(g_path,0);
		da.load_x(xl);
		da.load_rs(rs);
		da.save("a_graph.txt");
		return -1;
	}
	public  int test5() // gen TS w/ schedulable
	{
		String cf="a_cfg_list.txt";
		String ts="a_ts_list.txt";
		String xl="a_x_list.txt";
		Platform p=new Platform(g_path);
		p.setNum(100);
		p.genCfg_util(cf,95);
		p.setCheck();
		p.genTS(cf,ts,xl);
		return 0;
	}
	public  int test6() // ts --> simul rs
	{
		String path="sch/t1/";
		String ts="a_ts_list.txt";
		String rs="a_sim_list.txt";
		Platform p=new Platform(path);
		p.setProb(0.3);
		p.sim_loop(rs, ts, 2);
		return -1;
	}
	public  int test7()// simul rs --> graph
	{
		String xl="a_x_list.txt";
		String rs="a_sim_list.txt";
		DataSim ds=new DataSim(g_path,0);
		ds.load_x(xl);
		ds.load_rs(rs);
		ds.saveSim("a_sim_graph.txt");
		return 0;
	}
	public  int test8()
	{
		return 0;
	}
	public  int test9()
	{
		return 0;
	}
	public  int test10()
	{
		return 0;
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		z_auto1.init();
		Class c = z_auto1.class;
		z_auto1 m=new z_auto1();
		int[] aret=z_auto1.gret;
		if(g_idx==-1)
			S_TEngine.run(m,c,aret,10);
		else
			S_TEngine.runOnce(m,c,aret,g_idx,g_log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
