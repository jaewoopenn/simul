package testExp;
import part.ExpSimulMP;
import basic.TaskMng;
import exp.ExpSimul;
import gen.ConfigGen;
import simul.SimulInfo;
import simul.TaskSimul_EDF_VD;
import util.TEngine;

public class ExpSimul1 {
	public static int idx=5;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	public static int log_level=2;
	public int test1() 
	{
		ConfigGen cfg=new ConfigGen("test/cfg/cfg.txt");
		cfg.readFile();
		ExpSimul eg=new ExpSimul();
		eg.setCfg(cfg);
		TaskMng tm=eg.loadTM(0);
		
		SimulInfo si=eg.simul(new TaskSimul_EDF_VD(tm),1000);
		si.prn();
		return 1;
	}
	public int test2() 
	{
		ExpSimul eg=new ExpSimul();
		TaskMng tm=eg.loadTM("test/ts/taskset_0");
//		TaskMng tm=eg.loadTM("test/ts/taskset_1");

		SimulInfo si=eg.simul(new TaskSimul_EDF_VD(tm),1000);
		si.prn();
		return 1;
	}
	public int test3() 
	{
		ExpSimulMP eg=new ExpSimulMP(1);
		TaskMng tm=eg.loadTM("test/ts/taskset_0");
		eg.init(0,new TaskSimul_EDF_VD(tm));
		eg.simul(0,1000);
		eg.prn();
		return 1;
	}
	public  int test4() 
	{
		ExpSimulMP eg=new ExpSimulMP(2);
		TaskMng tm=eg.loadTM("test/ts/taskset_0");
		eg.init(0,new TaskSimul_EDF_VD(tm));
		tm=eg.loadTM("test/ts/taskset_0");
		eg.init(1,new TaskSimul_EDF_VD(tm));
		eg.simul(0,1000);
		eg.prn();
		return 1;
	}
	public  int test5() 
	{
		ExpSimulMP eg=new ExpSimulMP(2);
		TaskMng tm=eg.loadTM("test/ts/taskset_0");
		eg.init(0,new TaskSimul_EDF_VD(tm));
		tm=eg.loadTM("test/ts/taskset_2");
		eg.init(1,new TaskSimul_EDF_VD(tm));
		eg.simul(0,500);
		eg.move();
		eg.simul(500,1000);
		eg.prn();
		return 1;
	}
	public  int test6() 
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
		Class c = ExpSimul1.class;
		ExpSimul1 m=new ExpSimul1();
		int[] aret=ExpSimul1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
