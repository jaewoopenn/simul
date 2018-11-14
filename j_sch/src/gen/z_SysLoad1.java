package gen;
import basic.TaskSetFile;
import gen.ConfigGen;
import gen.SysGen;
import gen.SysGenTM;
import util.FUtil;
import util.Log;
import util.TEngine;

public class z_SysLoad1 {
	public static int idx=3;
	public static int log_level=1;
	public static int total=10;
	public static int gret[]={1,0,0,1,1, 1,0,0,0,0};
	public int test1() // gen
	{
		ConfigGen cfg=new ConfigGen("config/cfg1_copy.txt");
		cfg.readFile();
		String fn=cfg.get_fn();
		Log.prn(1, fn);
		FUtil f=new FUtil(fn);
		f.br_open();
		f.readSplit("------");
		TaskSetFile.loadView(f);

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
		Log.prn(1, fn);
		FUtil f=new FUtil(fn);
		f.br_open();
		while(true) {
			boolean b=f.readSplit("------");
			if(!b) break;
			TaskSetFile tsf=TaskSetFile.loadFile_in(f);
			tsf.getTM().prn();
		}

		return 0;
	}
	public  int test4() // load copy\
	{
		return 0;
	}
	public  int test5() //
	{
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
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
