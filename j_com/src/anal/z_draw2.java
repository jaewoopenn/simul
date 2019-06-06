package anal;

// draw rbf


import com.PRM;

import sample.TS1;
import task.TaskSet;
import util.SLog;
import util.SEngineT;

public class z_draw2 {

	public static void init_s() {
//		int s=1;
//		int s=2;
//		int s=3;
		int s=4;
		
//		int log=1;
		int log=2;
		
		s_idx=s;
		s_log_level=log;
	}
	
	public int test1() {
		TaskSet tm=TS1.tm2();
		tm.sort();
		PRM prm=new PRM(3,1.4);
		AnalDraw a=new AnalDraw(tm,prm);
		a.make("com/test_g.txt", 2, 30);
		return 1;
	}
	public int test2() {
		return 0;
	}
	public  int test3() {
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
		z_draw2.init_s();
		Class c = z_draw2.class;
		z_draw2 m=new z_draw2();
		int[] aret=z_draw2.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	private static int s_idx;
	private static int s_log_level;
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
