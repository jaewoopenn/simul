package autorun;

//\a_new\sch.py
// mc run

import auto.DataAnal;
import auto.Platform;
import util.SEngineT;


public class z_auto1 {
	private static int s_idx;
	private static int s_log_level;
	private String g_path;
	private double g_util_ul;
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
		g_path="run/mc1";
		g_num=5000;
//		g_num=100;
//		g_num=5;
		g_cf="a_cfg_list.txt";
		g_ts="a_ts_list.txt";
		g_xl="a_x_list.txt";
		
	}

	public void init_anal() {
		g_util_ul=1.00;
		g_rs="a_rs_list.txt";
		g_graph="a_graph.txt";
	}

	public int test1() 
	{
		init_g();
		init_anal();
		Platform p=new Platform(g_path);
		p.setNum(g_num);
		p.genCfg_util(g_cf,g_util_ul);
		p.genTS(g_cf,g_ts);
		int end=4;
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
