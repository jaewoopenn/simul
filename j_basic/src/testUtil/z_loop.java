package testUtil;

import util.MFile;
import util.MList;
import util.MLoop;
import util.SEngineT;
import util.SLog;

@SuppressWarnings("unused")
public class z_loop {
//	public static int idx=1;
//	public static int idx=2;
	public static int idx=3;
//	public static int idx=4;
	public static int log_level=1;
//	public static int log_level=2;


	
	public int test1()	{
		for(int i:MLoop.on(10)) {
			SLog.prn("hihi "+i);
		}
		return 0;
	}
	public int test2() {
		for(int i:MLoop.on(2,10)) {
			SLog.prn("hihi "+i);
		}
		return 0;
	}
	

	public int test3() {
		MLoop lp=MLoop.init();
		while(lp.until(4)) {
			SLog.prn("hihi");
		}
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
		Class c = z_loop.class;
		z_loop m=new z_loop();
		int[] aret=z_loop.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}