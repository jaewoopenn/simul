package Test;
import Util.FUtil;
import Util.Log;
import Util.TEngine;
import Simul.ConfigGen;
import Simul.SimGen;
import Simul.TaskMng;

public class SimGen2 {
	public static int log_level=2;
//	public static int idx=-1;
	public static int idx=6;
	public static int total=10;
	public static int gret[]={1,0,0,1,1, 1,0,0,0,0};
	public int test1() // gen
	{
		ConfigGen cfg=new ConfigGen();
		if (cfg.readFile("config/cfg1.txt")==0)
			return 0;
		SimGen eg=new SimGen(cfg);
		eg.gen();
		return 1;

	}
	public int test2() // load
	{
		ConfigGen cfg=new ConfigGen();
		if (cfg.readFile("config/cfg1.txt")==0)
			return 0;
		SimGen eg=new SimGen(cfg);
		eg.load2(5);
//		for(int i=3;i<6;i++){
//			Log.prn(2, "i---:"+i);
//			eg.load2(i);
//		}
		return 0;
	}
	public int test3() // load one
	{
		ConfigGen cfg=new ConfigGen();
		if (cfg.readFile("config/cfg1.txt")==0)
			return 0;
		SimGen eg=new SimGen(cfg);
//		eg.load(6);
		for(int i=6;i<8;i++){
			Log.prn(2, "i---:"+i);
			double v=eg.load3(i);
			Log.prn(2, "avg:"+v);
		}
		return 0;
	}
	public  int test4() // pick 1
	{
		Log.set_lv(1);
		ConfigGen cfg=new ConfigGen();
		if (cfg.readFile("config/cfg1.txt")==0)
			return 0;
		SimGen eg=new SimGen(cfg);
		TaskMng tm=eg.load_one(8);
		double v=eg.process2(tm,7);
		Log.prn(2, "avg:"+v);
		return 0;
	}
	public  int test5() //
	{
		ConfigGen cfg;
		for(int i=0;i<10;i++){
			cfg=new ConfigGen();
			if (cfg.readFile("cfg/cfgd_"+i+".txt")==0)
				return 0;
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
			cfg=new ConfigGen();
			if (cfg.readFile("cfg/cfgd_"+i+".txt")==0)
				return 0;
			SimGen eg=new SimGen(cfg);
			double avg=eg.load3(no);
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
	public static void main(String[] args) throws Exception {
		Class c = SimGen2.class;
		SimGen2 m=new SimGen2();
		int[] aret=SimGen2.gret;
		int sz=SimGen2.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
