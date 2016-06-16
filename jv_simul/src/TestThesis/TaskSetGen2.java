package TestThesis;
import Util.FUtil;
import Util.Log;
import Util.TEngine;
import Simul.ConfigCompGen;
import Simul.ConfigGen;
import Simul.SimGen;
import Simul.SimPartGen;

// mp

public class TaskSetGen2 {
	public static int log_level=2;
//	public static int log_level=1;
//	public static int idx=-1;
//	public static int idx=2;
	public static int idx=5;
//	public static int cpus=2;
	public static int cpus=4;
	public static double alpha=0.0;
	

	
	public static int total=10;
	public static int gret[]={1,1,1,0,1, 1,0,0,0,0};
	public int test1() // gen 1
	{
		ConfigCompGen cfg=new ConfigCompGen("com/cfg/mp_2_3.txt");
		cfg.readFile();
		SimPartGen eg=new SimPartGen(cfg);
		eg.gen();
			
		return 1;

	}
	public int test2() // gen set
	{
		ConfigCompGen cfg;
		for(int i=0;i<10;i++){
			cfg=new ConfigCompGen("com/cfg/mp_"+cpus+"_"+i+".txt");
			cfg.readFile();
			SimPartGen eg=new SimPartGen(cfg);
			Log.prn(2, i*5+50+"---");
			eg.gen();
			
		}
		return 1;
	}		
	public int test3() // load one
	{
		int method=4;
		ConfigCompGen cfg=new ConfigCompGen("com/cfg/mp_4_5.txt");
		cfg.readFile();
		SimPartGen eg=new SimPartGen(cfg);
		eg.set_alpha(alpha);
		eg.set_method(method);
		Log.prn(2, method+" "+alpha);
		boolean b=eg.load_one(15);
		if(b)
			Log.prn(2, "OK");
		else
			Log.prn(2, "Not");

		return 1;
	}
	public int test4() // load comp set
	{
		ConfigCompGen cfg=new ConfigCompGen("com/cfg/mp_3.txt");
		cfg.readFile();
		SimPartGen eg=new SimPartGen(cfg);
		eg.set_alpha(alpha);
		int tot=eg.size();
		int sum=eg.load();
		double suc=sum*1.0/tot;
		Log.prn(2, " suc:"+suc);
		return 1;
	}
	public int test5() // load various policies
	{
		for(int i=0;i<4;i++)
		{
			Log.prn(2, "method--"+i);
			anal(cpus,i);
		}
		return 0;
	}

	public int anal(int cpus,int no)
	{
		ConfigCompGen cfg;
		FUtil fu=new FUtil("com/rs/mp_"+cpus+"_"+no+".txt");
		for(int i=0;i<10;i++){
			cfg=new ConfigCompGen("com/cfg/mp_"+cpus+"_"+i+".txt");
			cfg.readFile();
			SimPartGen eg=new SimPartGen(cfg);
			eg.set_alpha(alpha);
			eg.set_method(no);
			int tot=eg.size();
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
