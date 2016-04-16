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
	public static int idx=7;
	public static int total=10;
	public static int gret[]={1,1,1,0,1, 1,0,0,0,0};
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
			Log.prn(2, "util:"+(i*5+30)+"%, suc:"+suc);
			fu.print(suc+"");
		}
		fu.save();
		return 1;
	}
	public int test3() // load one
	{
		ConfigGen cfg;
		cfg=new ConfigGen();
		if (cfg.readFile("tm/cfg/drop_70.txt")==0)
			return 0;
		SimGen eg=new SimGen(cfg);
		eg.gen();
		return 1;
	}
	public  int test4() // pick 1
	{
//		anal_drop(6);
		for(int i=6;i<8;i++)
		{
			anal_drop(i,"70");
		}
		return 0;
	}
	public int anal_drop(int no,String str)
	{
		ConfigGen cfg;
		FUtil fu=new FUtil("tm/rs/drop_"+str+"_"+no+".txt");
		cfg=new ConfigGen();
		if (cfg.readFile("tm/cfg/drop_"+str+".txt")==0)
			return 0;
		SimGen eg=new SimGen(cfg);
		for(int i=0;i<10;i++){
			double p=0.1/Math.pow(2, i);
			double avg=eg.load3(no,p);
			Log.prn(2, no+" p:"+p+" avg:"+avg);
			fu.print(avg+"");
		}
		fu.save();
		return 1;
	}	
	public  int test5() //
	{
		ConfigGen cfg;
		cfg=new ConfigGen();
		if (cfg.readFile("tm/cfg/cfg_0.txt")==0)
			return 0;
		SimGen eg=new SimGen(cfg);
//			Log.prn(2, i*5+50+"---");
		eg.prepare();
		for (int i=0;i<1000;i++){
			TaskMng tm=eg.genOne();
			int edf=eg.process(tm, 0);
			int edf_tm=eg.process(tm, 2);
			Log.prn(2, i+" "+edf+" "+edf_tm);
			if(edf_tm==0){
				tm.prn();
				break;
			}
		}
		return 1;
	}
	public  int test6() // 
	{
		ConfigGen cfg;
		cfg=new ConfigGen();
		if (cfg.readFile("tm/cfg/drop_90.txt")==0)
			return 0;
		SimGen eg=new SimGen(cfg);
		eg.gen2();
		return 1;
	}
	public  int test7()
	{
//		anal_drop(6);
		for(int i=6;i<8;i++)
		{
			anal_drop(i,"90");
		}
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
