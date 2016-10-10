package testThesis;
import anal.ConfigGen;
import anal.SimCompGen;
import anal.SimGen;
import utilSim.FUtil;
import utilSim.Log;
import utilSim.TEngine;

//com

public class TaskSetGen1 {
	public static int log_level=2;
//	public static int log_level=1;
//	public static int idx=-1;
//	public static int idx=1;
	public static int idx=5;
	
	public static int gret[]={1,1,1,0,1, 1,0,0,0,0};
	public int test1() // gen 1
	{
		ConfigGen cfg;
		cfg=new ConfigGen("com/cfg/cfg_6.txt");
		cfg.readFile();
		SimCompGen eg=new SimCompGen(cfg);
		eg.setMaxCom(3);
		eg.gen();
			
		return 1;

	}
	public int test2() // gen set
	{
		ConfigGen cfg;
		for(int i=0;i<10;i++){
			cfg=new ConfigGen("com/cfg/cfg_"+i+".txt");
			cfg.readFile();
			SimCompGen eg=new SimCompGen(cfg);
			eg.setMaxCom(3);
			
			Log.prn(2, i*5+50+"---");
			eg.gen();
			
		}
		return 1;
	}
	public int test3() // load one
	{
		double alpha=0.3;
		ConfigGen cfg=new ConfigGen("com/cfg/cfg_6.txt");
		cfg.readFile();
		SimCompGen eg=new SimCompGen(cfg);
		eg.set_alpha(alpha);
		Log.prn(2, ""+alpha);
		int ret=eg.load_one(0);
		if(ret==1)
			Log.prn(2, "OK");
		else
			Log.prn(2, "Not");

		return 1;	
	}
	public int test4() // load one
	{
		ConfigGen cfg;
		cfg=new ConfigGen("com/cfg/cfg_6.txt");
		cfg.readFile();
		SimCompGen eg=new SimCompGen(cfg);
		int tot=eg.size();
		for(int i=0;i<=10;i++){
			double alpha=i*1.0/10;
			eg.set_alpha(alpha);
			int sum=eg.load();
			double suc=sum*1.0/tot;
			Log.prnc(2, "alpha:"+alpha);
			Log.prn(2, " suc:"+suc);
		}
		return 1;
	}
	public int test5() // load
	{
		for(int i=0;i<4;i++)
		{
			anal(i);
		}
		return 0;
	}

	public int anal(int no)
	{
		double[] alphas={0,0.333,0.666,1};
		ConfigGen cfg;
		FUtil fu=new FUtil("com/rs/sim"+no+".txt");
		for(int i=0;i<10;i++){
			cfg=new ConfigGen("com/cfg/cfg_"+i+".txt");
			cfg.readFile();
			SimCompGen eg=new SimCompGen(cfg);
			int tot=eg.size();
			eg.set_alpha(alphas[no]);
			int sum=eg.load();
			double suc=sum*1.0/tot;
			Log.prn(2, "util:"+(i*5+50)+"%, suc:"+suc);
			fu.print(suc+"");
		}
		fu.save();
		return 1;
	}
	public int test6() // load one
	{
		ConfigGen cfg;
		cfg=new ConfigGen("tm/cfg/drop_80.txt");
		cfg.readFile();
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
		Class c = TaskSetGen1.class;
		TaskSetGen1 m=new TaskSetGen1();
		int[] aret=TaskSetGen1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
