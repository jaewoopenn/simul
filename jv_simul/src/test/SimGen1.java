package test;
import anal.ConfigGen;
import anal.SimGen;
import basic.TaskMng;
import utilSim.FUtil;
import utilSim.Log;
import utilSim.TEngine;

public class SimGen1 {
	public static int log_level=2;
//	public static int idx=-1;
	public static int idx=8;
	public static int total=10;
	public static int gret[]={1,0,0,1,1, 1,0,0,0,0};
	public int test1() // gen
	{
		ConfigGen cfg=new ConfigGen("config/cfg1.txt");
		cfg.readFile();
		SimGen eg=new SimGen(cfg);
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
		ConfigGen cfg=new ConfigGen("config/cfg1_copy.txt");
		cfg.readFile();
		SimGen eg=new SimGen(cfg);
		eg.gen();
		return 1;
	}
	public  int test5() //
	{
		ConfigGen cfg;
		for(int i=0;i<10;i++){
			cfg=new ConfigGen("cfg/cfg_"+i+".txt");
			cfg.readFile();
			SimGen eg=new SimGen(cfg);
			eg.gen();
			
		}
		return 1;
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
		Class c = SimGen1.class;
		SimGen1 m=new SimGen1();
		int[] aret=SimGen1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
