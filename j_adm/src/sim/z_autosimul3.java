package sim;

import anal.Anal;
import anal.AnalEDF_VD_ADM;
import anal.AnalEDF_VD_IMC;
import gen.SysLoad;
import imc.*;
import task.DTaskVec;
import util.SEngineT;
import util.SLog;
import util.SLogF;

// 테스트는 여기에서.

@SuppressWarnings("unused")
public class z_autosimul3 {
	public static int idx=1;
//	public static int idx=2;
//	public static int idx=3;
//	public static int idx=4;
	
	public static int log_level=1;
//	public static int log_level=2;


	
	public int test1()	{
		String tsn="adm/sim/taskset_75.txt";
		int n=3;
		int dur=2000;
		double prob=0.3;
		
		String out="adm/test.txt";
		SysLoad sy=new SysLoad(tsn);
		sy.open();
		sy.moveto(n);
		DTaskVec dt=sy.loadOne2();
		DoSimul ds=new DoSimul(0);
		ds.setProb(prob);
		ds.setDur(dur);
		ds.run(dt);
		ds.prn();
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
		Class c = z_autosimul3.class;
		z_autosimul3 m=new z_autosimul3();
		int[] aret=z_autosimul3.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}