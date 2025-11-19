package task;


import java.util.Vector;

import util.SEngineT;
import util.SLog;

public class z_task1 {
	public static void init_s() {
		s_idx=1;
//		s_idx=2;
//		s_idx=3;
		
		
		s_log_level=1;
	}

	public int test1() 
	{
		Vector<Task> tasks=new Vector<Task>();
		
		tasks.add(new Task(3,1,3));
		tasks.add(new Task(4,1,4));
		SLog.prn("hihi "+tasks.size());
		return -1;
	}

	public int test2() {
		TaskVec ts=new TaskVec();
		ts.add(new Task(3,1,3));
		ts.add(new Task(4,1,4));
		TaskSet tm=new TaskSet(ts);
		tm.prn();
		return -1;
	}
	public int test3() {
		return -1;
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
		z_task1.init_s();
		Class c = z_task1.class;
		z_task1 m=new z_task1();
		int[] aret=z_task1.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	private static int s_idx;
	private static int s_log_level;

}
