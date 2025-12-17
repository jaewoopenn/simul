package gen;
import task.TaskMng;
import task.TaskSet;
import task.TaskUtil;
import task.TaskVec;
import util.SLog;
import util.SEngineT;

public class z_SysLoad1 {
	public static void init_s() {
		s_idx=1;
//		s_idx=2;
//		s_idx=3;
		
		
		s_log_level=1;
	}

	// load
	public int test1() 
	{
		int n=25;

		String tsn="adm/test1/taskset_83.txt";
		
		SysLoad sy=new SysLoad(tsn);
		sy.moveto(n);
		TaskVec dt=sy.loadOne();
		TaskSet its=new TaskSet(dt.getVec());
		TaskMng tm=its.getTM();
		TaskUtil.prn(tm);
		SLog.prn(1, "----");
		return 1;

	}

	public int test2() 
	{
		return 0;
	}
	
	public int test3() 
	{

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
		z_SysLoad1.init_s();
		Class c = z_SysLoad1.class;
		z_SysLoad1 m=new z_SysLoad1();
		int[] aret=z_SysLoad1.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	private static int s_idx;
	private static int s_log_level;

}
