package anal;

import auto.DataAnal_IMC;
import auto.DataSim_IMC;
import gen.SysLoad;
import sim.AutoSimul;
import sim.DoSimul;
import task.DTaskVec;
import task.TaskMng;
import task.TaskSet;
import util.MList;
import util.SEngineT;
import util.SLog;

public class z_autoanal2 {
	public static int idx=1;
//	public static int idx=2;
//	public static int idx=3;
	public static int log_level=1;

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
		g_rs="a_rs_list.txt";
		g_graph="a_graph.txt";

		
	}



	public void simul(String rs_path) {
		MList fu=new MList();
		for(int i=0;i<2;i++) {
			DoAnal da=new DoAnal(i);
			AutoAnal as=new AutoAnal("adm/test1",da);
			as.setRS(rs_path);
			String rs=as.analList("a_ts_list.txt");	
			fu.add(rs);
		}
		fu.saveTo(rs_path+"/"+g_rs);
	}	

	public int test1()	{
		init_g();
		String rs_path="adm/anal";
		simul(rs_path);
		DataAnal_IMC da=new DataAnal_IMC(g_path,0);
		da.load_x(g_xl);
		da.load_rs(g_rs);
		da.save(g_graph);
		return 0;		
	}
	
	public int test2()	{
		return 0;
	}

	



	public int test3() {
		return -1;
	}
	public int test4() {
		return -1;
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
		Class c = z_autoanal2.class;
		z_autoanal2 m=new z_autoanal2();
		int[] aret=z_autoanal2.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}