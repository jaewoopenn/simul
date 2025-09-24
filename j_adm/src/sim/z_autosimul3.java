package sim;

import anal.DoAnal;
import gen.SysLoad;
import task.DTUtil;
import task.DTaskVec;
import util.SEngineT;
import util.SLog;

// 테스트는 여기에서.

@SuppressWarnings("unused")
public class z_autosimul3 {
	public static void init_s() {
//		s_idx=1;
		s_idx=2;
//		s_idx=3;
//		s_idx=4;
		
		
		s_log_level=1;
//		s_log_level=2;
	}
	public String g_tsn;
	public int g_idx;
	public int g_dur;
		

	public void init() {
		g_tsn="adm/sim/taskset_78.txt";
		g_idx=3;
		g_dur=5000;
	}
	public int test1()	{
		init();
		DTaskVec dt=getDT(g_idx);
		
		DoAnal da=new DoAnal(0);
		da.run(dt);

		return 0;
	}
	
	public int test2() {
		init();
		double prob=0.4;
		DTaskVec dt=getDT(g_idx);
		init();
//		DTUtil.prn(dt);
		String out="adm/test.txt";

		DoSimul ds=new DoSimul(0,g_dur,prob);
		ds.setTrace(out);
		ds.run(dt);
		
		SimulInfo si=ds.getSI();
		si.prn();
		return 0;
	}
	public int test3() {
		init();
		double prob=0.4;
		SysLoad sy=new SysLoad(g_tsn);
		DTaskVec dt= sy.loadOne();
		int i=0;
		while(dt!=null) {
			SLog.prn(2,"#### no: "+i+" #####");
			DoSimul ds=new DoSimul(0,g_dur,prob);
			ds.run(dt);
			SimulInfo si=ds.getSI();
			si.prn();
			
			dt= sy.loadOne();
			i++;
		}
		return 0;
	}
	
	public DTaskVec getDT(int n) {
		
		SysLoad sy=new SysLoad(g_tsn);
		sy.moveto(n);
		DTaskVec dt= sy.loadOne();
		if(dt==null) {
			SLog.prn("out of range ");
		}
		return dt;
	}

	public  int test4() {
		init();
		int dur=5000;
		double prob=0.4;
		DTaskVec dt=getDT(g_idx);
		DTUtil.prn(dt);
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
		z_autosimul3.init_s();
		Class c = z_autosimul3.class;
		z_autosimul3 m=new z_autosimul3();
		int[] aret=z_autosimul3.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	private static int s_idx;
	private static int s_log_level;
}