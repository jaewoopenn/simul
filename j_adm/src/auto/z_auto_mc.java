package auto;

import anal.AutoAnal;
import anal.DataAnal_IMC;
import anal.DoAnal;
import util.MList;

// generate task set skip if HC util = 0 or LC util=0 (not yet implement)
//\a_new\sch.py
// mc run

import util.SEngineT;


public class z_auto_mc {
	private static int s_idx;
	private static int s_log_level;
	private String g_path;
	private int g_st;
	private int g_step;
	private int g_stage;
	private int g_end;
	private int g_num;
	private int g_sort;
	private String g_cf;
	private String g_ts;
	private String g_xl;
	private String g_rs;
	private String g_graph;
	
	public static void init_s() {
		int s;
		s=1;
//		s=2;
//		s=3;
//		s=4;
		s_idx=s;
		
		s_log_level=2;
	}
	
	public void init_g() {
		g_path="adm/test2";
		g_sort=4;
		g_num=5000;
//		g_num=500;
//		g_num=20;
		g_cf="a_cfg_list.txt";
		g_ts="a_ts_list.txt";
		g_xl="a_x_list.txt";
	}

	public void init_anal() {
//		g_stage=1;
		g_stage=3;
//		g_stage=6;
		g_st=56;
		g_step=3;
		g_end=100;
		g_rs="a_rs_list.txt";
		g_graph="a_graph.txt";
	}
	public void anal() {
		MList fu=MList.new_list();
		for(int i=0;i<g_sort;i++) {
			DoAnal da=new DoAnal(i);
			da.setMC();
			AutoAnal as=new AutoAnal(g_path,da);
			String rs=as.analList(g_ts);	
			fu.add(rs);
		}
		fu.saveTo(g_path+"/"+g_rs);
	}	
	
	public int test1() { // MC
		init_g();
		init_anal();
		AutoParConfig apg=AutoParConfig.init();
		apg.num=g_num;
		apg.setMC();
		AutoConfig a=new AutoConfig(g_path,apg);
		a.genCfg_util(g_cf,g_st,g_step,g_end);

		AutoSysGen p=new AutoSysGen(g_path);
		p.setStage(g_stage);
		p.genTS(g_cf,g_ts);
		p.genXA(g_cf,g_path+"/"+g_xl);
		anal();

		DataAnal_IMC da=new DataAnal_IMC(g_path,0);
		da.setMC();
		da.load_x(g_xl);
		da.load_rs(g_rs);
		da.save(g_graph);
		
		return 0;
	}
	public int test2() 	{  // without gen
		return 0;
	}
	public int test3()  { // gen only
		return 0;
	}
	public  int test4() {
		return 0;
	}
	public  int test5() {
		return 0;
	}
	public  int test6() {
		return -1;
	}
	public  int test7() {
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
