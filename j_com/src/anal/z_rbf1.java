package anal;


import util.SLog;
import task.Task;
import task.TaskSet;
import task.TaskVec;
import util.SEngineT;

public class z_rbf1 {
	public static int log_level=1;
	public static int idx=1;
	public int test1()
	{
		TaskVec ts=new TaskVec();
		ts.add(new Task(3,1));
		ts.add(new Task(4,1));
		TaskSet tm=new TaskSet(ts);
		
		for(int t=0;t<12;t++) {
			double r=tm.computeRBF(2,t);
			SLog.prnc(1, "t:"+t);
			SLog.prn(1, " r:"+r);
		}
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
		Class c = z_rbf1.class;
		z_rbf1 m=new z_rbf1();
		int[] aret=z_rbf1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={2,23,0,0,0,0,0,0,0,0};

}
