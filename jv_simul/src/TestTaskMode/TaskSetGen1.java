package TestTaskMode;
import Util.FUtil;
import Util.Log;
import Util.TEngine;
import Simul.ConfigGen;
import Simul.SimGen;
import Simul.TaskMng;

public class TaskSetGen1 {
	public static int log_level=2;
//	public static int idx=-1;
	public static int idx=2;
	public static int total=10;
	public static int gret[]={1,1,0,0,0, 0,0,0,0,0};
	public int test1() // gen
	{
		ConfigGen cfg;
		for(int i=0;i<10;i++){
			cfg=new ConfigGen();
			if (cfg.readFile("tm/cfg/cfg_"+i+".txt")==0)
				return 0;
			SimGen eg=new SimGen(cfg);
//			Log.prn(2, i*5+50+"---");
			eg.gen();
			
		}
		return 1;

	}
	public int test2() // load
	{
		for(int i=0;i<4;i++)
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
			Log.prn(2, "util:"+(i*5+50)+"%, suc:"+suc);
			fu.print(suc+"");
		}
		fu.save();
		return 1;
	}
	
	public int anal2(int no)
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
	public int test3() // load one
	{
		return 0;
	}
	public  int test4() // pick 1
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
		Class c = TaskSetGen1.class;
		TaskSetGen1 m=new TaskSetGen1();
		int[] aret=TaskSetGen1.gret;
		int sz=TaskSetGen1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}