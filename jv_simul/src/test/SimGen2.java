package test;
import basic.TaskMng;
import simul.ConfigGen;
import simul.SimAnal;
import simul.SimGen;
import utilSim.FUtil;
import utilSim.Log;
import utilSim.TEngine;

public class SimGen2 {
	public static int log_level=2;
//	public static int idx=-1;
	public static int idx=5;
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
		ConfigGen cfg=new ConfigGen("config/cfg1.txt");
		cfg.readFile();
		SimAnal eg=new SimAnal(cfg);
		eg.load2(5);
//		for(int i=3;i<6;i++){
//			Log.prn(2, "i---:"+i);
//			eg.load2(i);
//		}
		return 0;
	}
	public int test3() // load one
	{
		ConfigGen cfg=new ConfigGen("config/cfg1.txt");
		cfg.readFile();
		SimAnal eg=new SimAnal(cfg);
//		eg.load(6);
		for(int i=6;i<8;i++){
			Log.prn(2, "i---:"+i);
			double v=eg.load3(i,0.05);
			Log.prn(2, "avg:"+v);
		}
		return 0;
	}
	public  int test4() // pick 1
	{
		Log.set_lv(1);
		ConfigGen cfg=new ConfigGen("config/cfg1.txt");
		cfg.readFile();
		SimAnal eg=new SimAnal(cfg);
		TaskMng tm=eg.load_one(8);
		double v=eg.process2(tm,7,0.05);
		Log.prn(2, "avg:"+v);
		return 0;
	}
	public  int test5() //
	{
		ConfigGen cfg;
		for(int i=0;i<10;i++){
			cfg=new ConfigGen("cfg/cfgd_"+i+".txt");
			cfg.readFile();
			SimGen eg=new SimGen(cfg);
			eg.gen();
			
		}
		return 1;
	}
	public  int test6() // 
	{
		for(int i=6;i<8;i++)
		{
			anal(i);
		}
		return 0;
	}
	public int anal(int no)
	{
		ConfigGen cfg;
		FUtil fu=new FUtil("rs/simd"+no+".txt");
		for(int i=0;i<10;i++){
			cfg=new ConfigGen("cfg/cfgd_"+i+".txt");
			cfg.readFile();
			SimAnal eg=new SimAnal(cfg);
			double avg=eg.load3(no,0.05);
			Log.prn(2, "util:"+(i*5+30)+"%, avg:"+avg);
			fu.print(avg+"");
		}
		fu.save();
		return 1;
		
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
		Class c = SimGen2.class;
		SimGen2 m=new SimGen2();
		int[] aret=SimGen2.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
