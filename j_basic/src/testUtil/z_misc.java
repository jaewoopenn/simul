package testUtil;

import util.MLoop;
import util.MProg;
import util.MRand;
import util.SEngineT;
import util.SLog;

public class z_misc {
//	public static int idx=1;
	public static int idx=2;
//	public static int idx=3;
//	public static int idx=4;

	public static int log_level=1;

	
	public int test1()	{
		MRand mr=MRand.init();
		int i=mr.getInt(10);
		SLog.prn("rs: "+i);
		return 0;
	}
	public int test2() {
		MProg prg=MProg.init(200);
//		prg.setPercent();   // plan 1
		prg.setVerbose(3);  // plan 2
		MLoop.reset();
		while(MLoop.until(200)) {
			prg.inc();
		}
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
		Class c = z_misc.class;
		z_misc m=new z_misc();
		int[] aret=z_misc.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}