package sim;

import anal.DoAnal;
import gen.SysLoad;
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
		
		
		s_log_level=1;
//		s_log_level=2;
	}

		

	
	public int test1()	{
		DTaskVec dt=getDT();
		
		DoAnal da=new DoAnal(0);
		da.run(dt);

		return 0;
	}
	
	public int test2() {
		int dur=1500;
		double prob=0.4;
		DTaskVec dt=getDT();
		String out="adm/test.txt";

		DoSimul ds=new DoSimul(0);
//		ds.setTrace(out);
		ds.setProb(prob);
		ds.setDur(dur);
		ds.run(dt);
		ds.prn();
		return 0;
	}
	
	public DTaskVec getDT() {
//		String tsn="adm/test1/task.txt";
		String tsn="adm/sim/taskset_93.txt";
		int n=4;
		
		SysLoad sy=new SysLoad(tsn);
		sy.open();
		sy.moveto(n);
		DTaskVec dt= sy.loadOne();
		if(dt==null) {
			SLog.prn("out of range ");
		}
		return dt;
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