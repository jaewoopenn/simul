package Test;
import Util.FUtil;
import Util.Log;
import Util.TEngine;
import Simul.ConfigGen;
import Simul.SimGen;

public class SimGen1 {
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
		int tot=eg.size();
		int sum=eg.load();
		Log.prn(2, "suc:"+sum+"/"+tot);
		if(total==sum)
			return 1;
		return 0;
	}
	public int test3() // load one
	{
		ConfigGen cfg=new ConfigGen();
		if (cfg.readFile("config/cfg1.txt")==0)
			return 0;
		SimGen eg=new SimGen(cfg);
		int ret=eg.load(99);
		Log.prn(2, "ret:"+ret);
		return ret;
	}
	public  int test4() // load copy\
	{
		ConfigGen cfg=new ConfigGen();
		if (cfg.readFile("config/cfg1_copy.txt")==0)
			return 0;
		SimGen eg=new SimGen(cfg);
		eg.gen();
		return 1;
	}
	public  int test5() //
	{
		ConfigGen cfg;
		for(int i=0;i<10;i++){
			cfg=new ConfigGen();
			if (cfg.readFile("cfg/cfg_"+i+".txt")==0)
				return 0;
			SimGen eg=new SimGen(cfg);
			eg.gen();
			
		}
		return 1;
	}
	public  int test6() // 
	{
		ConfigGen cfg;
		FUtil fu=new FUtil("rs/sim1.txt");
		for(int i=0;i<10;i++){
			cfg=new ConfigGen();
			if (cfg.readFile("cfg/cfg_"+i+".txt")==0)
				return 0;
			SimGen eg=new SimGen(cfg);
			int tot=eg.size();
			int sum=eg.load();
			double suc=sum*1.0/tot;
			Log.prn(2, "util:"+(i*10+10)+"%, suc:"+suc);
			fu.print(suc+"");
		}
		fu.end();
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
		Class c = SimGen1.class;
		SimGen1 m=new SimGen1();
		int[] aret=SimGen1.gret;
		int sz=SimGen1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
