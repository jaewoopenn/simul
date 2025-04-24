package task;
import task.Task;
import task.TaskMng;
import util.SEngineT;
import util.SLog;
import z_ex.TS_MC2;

public class z_TaskSet1 {
	public static int idx=1;
	public static int log_level=1;
	
	public int test1() // reverse order test
	{
		TaskMng tm=TS_MC2.ts1();
		tm.prn();
		Task[] ts=tm.getTasks();
		SLog.prn(1,"------------");
		for(int i=ts.length-1;i>=0;i--){
			Task t=ts[i];
			t.prnShort();
		}
		
		return 1;
	}
	public int test2()
	{
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
		Class c = z_TaskSet1.class;
		z_TaskSet1 m=new z_TaskSet1();
		int[] aret=z_TaskSet1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}