package auto;

import anal.Anal;

// Platform 으로 가기 위해서.. 
// auto run 4

import anal.AnalEDF_VD_ADM;
import anal.AnalEDF_VD_IMC;
import anal.DoAnal;
import gen.SysLoad;
import imc.*;
import sim.DoSimul;
import task.DTaskVec;
import util.MList;
import util.SEngineT;
import util.SLog;
import util.SLogF;

@SuppressWarnings("unused")
public class z_autosimul2 {
	public static int idx=1;
//	public static int idx=2;
//	public static int idx=3;
//	public static int idx=4;
//	public static int log_level=1;
	public static int log_level=2;

	private String g_path;
	private String g_cf;
	private String g_ts;
	private String g_xl;
	private String g_rs;
	private String g_graph;
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
		Class c = z_autosimul2.class;
		z_autosimul2 m=new z_autosimul2();
		int[] aret=z_autosimul2.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}