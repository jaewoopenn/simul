package auto;

import anal.AutoAnal;
import anal.DataAnal_IMC;
import anal.DoAnal;
import util.MList;

// generate task set skip if HC util = 0 or LC util=0 (not yet implement)
//\a_new\sch.py
// mc run

import util.SEngineT;


public class z_auto_imc {
	private static int s_idx;
	private static int s_log_level;
	private String g_path;
	private int g_st;
	private int g_step;
	private int g_stage;
	private int g_end;
	private int g_num;
	private int g_sort_max;
	private String g_cf;
	private String g_ts;
	private String g_xl;
	private String g_rs;
	private String g_graph;
	
	public static void init_s() {
//		s_idx=1;
//		s_idx=2;
//		s_idx=3;
		s_idx=4;
//		s_idx=5;
		
		
		s_log_level=2;
	}
	
	public void init_g() {
		g_path="adm/anal";
//		g_num=5000;
//		g_num=500;
		g_num=30;
		g_cf="a_cfg_list.txt";
		g_ts="a_ts_list.txt";
		g_xl="a_x_list.txt";
		g_sort_max=4; // adm, edf-vd, edf, amc
	}

	public void init_anal() {
		g_stage=1;
//		g_stage=3;
//		g_stage=6;
		g_st=56;
		g_step=3;
		g_end=100;
		g_rs="a_rs_list.txt";
		g_graph="a_graph.txt";
	}
	public void anal() {
		MList fu=MList.new_list();
		for(int i=0;i<g_sort_max;i++) {
			DoAnal da=new DoAnal(i);
			AutoAnal as=new AutoAnal(g_path,da);
			as.setRS(g_path);
			String rs=as.analList(g_ts);	
			fu.add(rs);
		}
		fu.saveTo(g_path+"/"+g_rs);
		DataAnal_IMC da=new DataAnal_IMC(g_path,0);
		da.load_x(g_xl);
		da.load_rs(g_rs);
		da.save(g_graph);
	}	
	public void anal_util() {
		DoAnal da=new DoAnal(99);
		AutoAnal as=new AutoAnal(g_path,da);
		as.setRS(g_path);
		as.analList(g_ts);	
	}	
	public void gen() {
		AutoParConfig apg=AutoParConfig.init();
		apg.num=g_num;
		AutoConfig a=new AutoConfig(g_path,apg);
		a.genCfg_util(g_cf,g_st,g_step,g_end);
		AutoSysGen p=new AutoSysGen(g_path);
		p.setStage(g_stage);
		p.genTS(g_cf,g_ts);
		p.genXA(g_cf,g_path+"/"+g_xl);
		
	}
	
	public int test1() {
		init_g();
		init_anal();
		gen();
		anal();

		return 0;
	}
	public int test2() 	{  // without gen
		init_g();
		init_anal();
		anal();

		return 0;
	}
	public int test3()  { // gen only
		init_g();
		init_anal();
		gen();
		return 0;
	}
	public  int test4() {
		init_g();
		init_anal();
		anal_util();
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
		z_auto_imc.init_s();
		Class c = z_auto_imc.class;
		z_auto_imc m=new z_auto_imc();
		int[] aret=z_auto_imc.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
