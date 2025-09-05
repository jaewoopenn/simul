package gen;
import task.DTaskVec;
import task.TaskMng;
import task.TaskSet;
import util.SLog;
import util.SEngineT;

public class z_SysLoad1 {
	public static int idx=1;
//	public static int idx=2;
	public static int log_level=1;

	// load
	public int test1() 
	{
		int n=25;

		String tsn="adm/test1/taskset_83.txt";
		
		SysLoad sy=new SysLoad(tsn);
		String ret=sy.open();
		SLog.prn(0, ret);
		sy.moveto(n);
		DTaskVec dt=sy.loadOne2();
		for(int i=0;i<dt.getStageNum();i++) {
			SLog.prn(1, "time: "+dt.getTime(i));
			TaskSet its=new TaskSet(dt.getVec(i));
			TaskMng tm=its.getTM();
			tm.prn();
			SLog.prn(1, "----");
		}
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
		Class c = z_SysLoad1.class;
		z_SysLoad1 m=new z_SysLoad1();
		int[] aret=z_SysLoad1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
