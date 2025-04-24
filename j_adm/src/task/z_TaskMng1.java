package task;

import gen.SysLoad;
import util.SEngineT;
import z_ex.TS_MC1;

public class z_TaskMng1 {
	public static int log_level=1;
	public static int idx=1;
	public int test1()
	{
		SysLoad sy=new SysLoad("test/t1/taskset_65");
		sy.open();
		TaskMng tm=sy.loadOne();
		tm.prnInfo();
		return -1;
	}
	public int test2() //vd
	{
		TaskMng tm=TS_MC1.ts1();
		tm.prnInfo();
		return -1;
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
		Class c = z_TaskMng1.class;
		z_TaskMng1 m=new z_TaskMng1();
		int[] aret=z_TaskMng1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}