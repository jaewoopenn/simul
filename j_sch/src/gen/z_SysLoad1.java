package gen;
import gen.ConfigGen;
import task.TaskMng;
import util.SLog;
import util.SEngineT;

public class z_SysLoad1 {
	public static int idx=4;
	public static int log_level=1;

	public int test1() // gen
	{
		ConfigGen cfg=new ConfigGen("config/cfg1_copy.txt");
		cfg.readFile();
		String fn=cfg.get_fn();
		SLog.prn(1, fn);


		return 1;

	}
	public  int test2() // load copy\
	{
		SysLoad sy=new SysLoad("sch/t1/taskset_65");
		sy.open();
		TaskMng tm=sy.loadOne();
		tm.prnInfo();
		return 0;
	}
	public  int test3() //
	{
		SysLoad sy=new SysLoad("config/cfg1_copy.txt");
		sy.open();
		while(true) {
			TaskMng tm=sy.loadOne();
			if(tm==null) break;
			tm.prnInfo();
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
