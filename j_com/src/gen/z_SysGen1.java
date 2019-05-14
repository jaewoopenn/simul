package gen;
import gen.ConfigGen;
import gen.SysGen;
import util.S_TEngine;

public class z_SysGen1 {
	public static int idx=1;
//	public static int idx=2;
	public static int log_level=1;
	public static int total=10;
	public static int gret[]={1,0,0,1,1, 1,0,0,0,0};

	// gen
	public int test1() 
	{
		ConfigGen cfg=new ConfigGen();
		cfg.load("com/cfg1_copy.txt");
		SysGen eg=new SysGen(cfg);
		String fn=cfg.get_fn();
		eg.gen(fn);
		return 1;

	}

	// gen with schedulable 
	public int test2() 
	{
		return 0;
	}
	
	public int test3() 
	{
		return 0;
	}
	public  int test4() // 
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
			S_TEngine.run(m,c,aret,10);
		else
			S_TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
