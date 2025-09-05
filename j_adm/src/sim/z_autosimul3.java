package sim;

import anal.Anal;
import anal.AnalEDF_VD_ADM;
import anal.AnalEDF_VD_IMC;
import anal.DoAnal;
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

	public DTaskVec getDT() {
//		String tsn="adm/test1/task.txt";
		String tsn="adm/anal/taskset_95.txt";
		int n=4;
		
		String out="adm/test.txt";
		SysLoad sy=new SysLoad(tsn);
		sy.open();
		sy.moveto(n);
		DTaskVec dt= sy.loadOne2();
		if(dt==null) {
			SLog.prn("out of range ");
		}
		return dt;
	}
		

	
	public int test1()	{
		DTaskVec dt=getDT();
		
		DoAnal da=new DoAnal(0);
		da.run(dt);

		return 0;
	}
	public int test2() {
		int dur=300;
		double prob=0.3;
		DTaskVec dt=getDT();
		

		DoSimul ds=new DoSimul(0);
//		ds.setTrace(out);
		ds.setProb(prob);
		ds.setDur(dur);
		ds.run(dt);
		ds.prn();
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