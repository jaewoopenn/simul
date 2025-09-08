package sim;

import anal.Anal;

// Platform 으로 가기 위해서.. 
// auto run 4

import auto.DataSim_IMC;
import util.MList;
import util.SEngineT;

@SuppressWarnings("unused")
public class z_autosimul2 {
	private String g_path;
	private String g_cf;
	private String g_ts;
	private String g_xl;
	private String g_rs;
	private String g_graph;
	public static void init_s() {
//		s_idx=1;
		s_idx=2;
//		s_idx=3;
		
		
		s_log_level=1;
//		s_log_level=2;
	}

	public void init_g() {
		g_path="adm/test1";
		g_cf="a_cfg_list.txt";
		g_ts="a_ts_list.txt";
		g_xl="a_x_list.txt";
		g_rs="a_sim_list.txt";
		g_graph="a_sim_graph.txt";
		
	}



	public void simul(String rs_path) {
		MList fu=MList.new_list();
		for(int i=0;i<2;i++) {
			DoSimul ds=new DoSimul(i);
			ds.setProb(0.3);
			ds.setDur(1000);
			AutoSimul as=new AutoSimul(g_path,ds);
			as.setRS(rs_path);
			String rs=as.simulList(g_ts);
			fu.add(rs);
		}
		fu.saveTo(rs_path+"/"+g_rs);
	}
	
	public int test1()	{
		init_g();
		String rs_path="adm/pi0";
		simul(rs_path);
		DataSim_IMC ds=new DataSim_IMC(rs_path,0);
		ds.load_x(g_xl);
		ds.load_rs(g_rs);
		ds.saveSim(g_graph);
		return 0;
	}
	public int test2() {
		return 0;
	}
	

	public int test3() {
		return 0;
	}
	public  int test4() {
		return 1;
	}
	public  int test5() {
		return 1;
	}
	public  int test6() {
		return 1;
	}
	public  int test7() {
		return 1;
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
		z_autosimul2.init_s();
		Class c = z_autosimul2.class;
		z_autosimul2 m=new z_autosimul2();
		int[] aret=	z_autosimul2.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	private static int s_idx;
	private static int s_log_level;
}