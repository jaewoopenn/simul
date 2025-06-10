package autorun;

//\a_new\sch.py
// mc run

import auto.DataAnal;
import auto.Platform;
import util.SEngineT;


public class z_auto_mc {
	private static int s_idx;
	private static int s_log_level;
	private String g_path;
	private int g_num;
	private String g_cf;
	private String g_ts;
	private String g_xl;
	private String g_rs;
	private String g_graph;
	
	public static void init_s() {
		int s=0;
		s=1;
//		s=2;
		s_idx=s;
		
		s_log_level=2;
	}
	
	public void init_g() {
		g_path="test/mc";
		g_num=5000;
//		g_num=100;
//		g_num=5;
		g_cf="a_cfg_list.txt";
		g_ts="a_ts_list.txt";
		g_xl="a_x_list.txt";
		
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
		p.setNum(g_num);
		p.genCfg_util(g_cf,56,4,100);
		p.genTS(g_cf,g_ts);
		p.genXA(g_cf,g_xl);
//		int end=6;
		int end=3;
		p.anal_loop(g_rs,g_ts,end);
		DataAnal da=new DataAnal(g_path,0);
		da.load_x(g_xl);
		da.load_rs(g_rs);
		da.save(g_graph);
		return 0;
	}
	public int test2() // from 
	{
		return -1;	
	}
	public int test3() // task set --> anal rs
	{
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
		z_auto_mc.init_s();
		Class c = z_auto_mc.class;
		z_auto_mc m=new z_auto_mc();
		int[] aret=z_auto_mc.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
