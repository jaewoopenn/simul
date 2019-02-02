package auto;

// TODO make MC-Post scheduling algorithm in simulation

import util.TEngine;


public class z_auto1 {
//	public static int idx=1;
//	public static int idx=2;
//	public static int idx=3;
	public static int idx=4;
//	public static int idx=5;
//	public static int idx=6;
//	public static int idx=7;
	public static int log_level=1;

	private String g_path="sch/t1";
	
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
		ds.save("a_sim_graph.txt");
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
		Class c = z_auto1.class;
		z_auto1 m=new z_auto1();
		int[] aret=z_auto1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
