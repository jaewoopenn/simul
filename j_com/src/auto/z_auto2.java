package auto;


import util.SEngineT;


public class z_auto2 {
	private static int s_idx;
	private static int s_log_level;
	
	public static void init_s() {
//		int s=1;
//		int s=2;
		int s=3;
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


	public int test1()  {
		return 0;
	}
	public int test2() {
		return -1;	
	}
	
	public int test3() { 
		return -1;
	}
	public  int test4() { 
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
