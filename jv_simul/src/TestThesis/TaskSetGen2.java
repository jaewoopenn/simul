package TestThesis;
import Util.FUtil;
import Util.Log;
import Util.TEngine;
import Basic.TaskMng;
import Simul.ConfigCompGen;
import Simul.ConfigGen;
import Simul.SimCompGen;
import Simul.SimGen;
import Simul.SimPartGen;

// mp

public class TaskSetGen2 {
	public static int log_level=2;
//	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=1;
	public static int total=10;
	public static int gret[]={1,1,1,0,1, 1,0,0,0,0};
	public int test1() // gen 1
	{
		ConfigCompGen cfg=new ConfigCompGen("com/cfg/mp_1.txt");
		if (cfg.readFile()==0)
			return 0;
		SimPartGen eg=new SimPartGen(cfg);
		eg.gen();
			
		return 1;

	}
	public int test2() // gen set
	{
//		ConfigGen cfg;
//		for(int i=0;i<10;i++){
//			cfg=new ConfigGen();
//			if (cfg.readFile("com/cfg/cfg_"+i+".txt")==0)
//				return 0;
//			SimGen eg=new SimGen(cfg);
////			Log.prn(2, i*5+50+"---");
//			eg.gen();
//			
//		}
		return 1;
	}		
	public int test3() // load one
	{
		ConfigCompGen cfg=new ConfigCompGen("com/cfg/mp_1.txt");
		if (cfg.readFile()==0)
			return 0;
		SimPartGen eg=new SimPartGen(cfg);
		eg.load_one(0);
		return 1;
	}
	public int test4() // load one
	{
		ConfigGen cfg;
		cfg=new ConfigGen();
		if (cfg.readFile("com/cfg/cfg_6.txt")==0)
			return 0;
		SimCompGen eg=new SimCompGen(cfg);
		int tot=eg.size();
		for(int i=0;i<=10;i++){
			double alpha=i*1.0/10;
			int sum=eg.load(alpha);
			double suc=sum*1.0/tot;
			Log.prnc(2, "alpha:"+alpha);
			Log.prn(2, " suc:"+suc);
			
		}
		return 1;
	}
	public int test5() // load
	{
		for(int i=0;i<5;i++)
		{
			anal(i);
		}
		return 0;
	}

	public int anal(int no)
	{
		ConfigGen cfg;
		FUtil fu=new FUtil("tm/rs/sim"+no+".txt");
		for(int i=0;i<10;i++){
			cfg=new ConfigGen();
			if (cfg.readFile("tm/cfg/cfg_"+i+".txt")==0)
				return 0;
			SimGen eg=new SimGen(cfg);
			int tot=eg.size();
			int sum=eg.load(no);
			double suc=sum*1.0/tot;
			Log.prn(2, "util:"+(i*5+30)+"%, suc:"+suc);
			fu.print(suc+"");
		}
		fu.save();
		return 1;
	}
	public int test6() // load one
	{
		ConfigGen cfg;
		cfg=new ConfigGen();
		if (cfg.readFile("tm/cfg/drop_80.txt")==0)
			return 0;
		SimGen eg=new SimGen(cfg);
		eg.gen2();
		return 1;
	}
	public  int test7()
	{
		return -1;
	}
	public  int test8()
	{
		return -1;
	}
	public  int test9()
	{
		return -1;
	}
	public  int test10()
	{
		return -1;
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = TaskSetGen2.class;
		TaskSetGen2 m=new TaskSetGen2();
		int[] aret=TaskSetGen2.gret;
		int sz=TaskSetGen2.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
