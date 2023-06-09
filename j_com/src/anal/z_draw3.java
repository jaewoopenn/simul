package anal;

// draw DBF


import com.PRM;

import sample.TS2;
import task.TaskSet;
import util.SEngineT;

public class z_draw3 {

	public static void init_s() {
		int s=1;
//		int s=2;
//		int s=3;
//		int s=4;
		
//		int log=1;
		int log=2;
		
		s_idx=s;
		s_log_level=log;
	}
	
	public int test1() {
		TaskSet ts=TS2.tm1();
		ts.sort();
		AnalDraw a=new AnalDraw();
		a.draw_dbf(ts, 30);
		a.save("com/draw/test_g.txt");
		return 1;
	}
	public int test2() {
		PRM prm=new PRM(3,2.4);
		AnalDraw a=new AnalDraw();
		a.draw_sbf(prm, 30);
		a.save("com/test_g.txt");

		return 1;
	}
	public  int test3() {
		TaskSet ts=TS2.tm1();
		ts.sort();
		PRM prm=new PRM(3,2.2);
		AnalDraw a=new AnalDraw();
		a.draw_rbf(ts, 2, 30);
		a.draw_sbf(prm, 30);
		a.save("com/test_g.txt");
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
		z_draw3.init_s();
		Class c = z_draw3.class;
		z_draw3 m=new z_draw3();
		int[] aret=z_draw3.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	private static int s_idx;
	private static int s_log_level;
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
