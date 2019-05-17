package gen;
import basic.TaskMng;
import basic.TaskSetEx;
import gen.ConfigGen;
import util.MFile;
import util.S_Log;
import util.S_TEngine;

public class z_SysLoad1 {
	public static int idx=5;
	public static int log_level=1;
	public static int total=10;
	public static int gret[]={1,0,0,1,1, 1,0,0,0,0};
	public int test1() // gen
	{
		ConfigGen cfg=new ConfigGen("config/cfg1_copy.txt");
		cfg.readFile();
		String fn=cfg.get_fn();
		S_Log.prn(1, fn);
		MFile f=new MFile(fn);
		f.br_open();
		f.readSplit("------");
		TaskSetEx.loadView(f);

		return 1;

	}
	public int test2() // load
	{
		SysLoad sy=new SysLoad("config/cfg1_copy.txt");
		sy.load();
		return 0;
	}
	public int test3() // load one
	{
		ConfigGen cfg=new ConfigGen("config/cfg1_copy.txt");
		cfg.readFile();
		String fn=cfg.get_fn();
		S_Log.prn(1, fn);
		MFile f=new MFile(fn);
		f.br_open();
		while(true) {
			boolean b=f.readSplit("------");
			if(!b) break;
			TaskSetEx tsf=TaskSetEx.loadFile_in(f);
			tsf.getTM().prn();
		}

		return 0;
	}
	public  int test4() // load copy\
	{
		SysLoad sy=new SysLoad("config/cfg1_copy.txt");
		sy.open();
		TaskMng tm=sy.loadOne();
		tm.prnInfo();
		return 0;
	}
	public  int test5() //
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
	public  int test6() // 
	{
		return 0;
	}
	public  int test7()
	{
		return 0;
	}
	public  int test8()
	{
		return 0;
	}
	public  int test9()
	{
		return 0;
	}
	public  int test10()
	{
		return 0;
	}

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = z_SysLoad1.class;
		z_SysLoad1 m=new z_SysLoad1();
		int[] aret=z_SysLoad1.gret;
		if(idx==-1)
			S_TEngine.run(m,c,aret,10);
		else
			S_TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
