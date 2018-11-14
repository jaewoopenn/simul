package gen;
import basic.TaskSetFile;
import gen.ConfigGen;
import gen.SysGen;
import gen.SysGenTM;
import util.FUtil;
import util.Log;
import util.TEngine;

public class z_SysGen1 {
	public static int log_level=1;
	public static int idx=2;
	public static int total=10;
	public static int gret[]={1,0,0,1,1, 1,0,0,0,0};
	public int test1() // gen
	{
		ConfigGen cfg=new ConfigGen("config/cfg1_copy.txt");
		cfg.readFile();
		SysGen eg=new SysGenTM(cfg);
		eg.gen();
		return 1;

	}
	public int test2() // load
	{
		return 0;
	}
	public int test3() // load one
	{
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
		Class c = z_SysGen1.class;
		z_SysGen1 m=new z_SysGen1();
		int[] aret=z_SysGen1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
