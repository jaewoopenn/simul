package testExp;
import basic.TaskMng;
import exp.ExpSimul;
import gen.ConfigGen;
import simul.SimulInfo;
import simul.TaskSimul;
import simul.TaskSimul_EDF_AD;
import simul.TaskSimul_EDF_VD;
import util.Log;
import util.TEngine;

public class ExpSimul1 {
	public static int idx=2;
//	public static int idx=-1;
	public static int gret[]={1,1,0,6,0,0,0,0,0,0};
	public static int log_level=2;
	public int test1() 
	{
		ConfigGen cfg=new ConfigGen("test/cfg/cfg.txt");
		cfg.readFile();
//		cfg.prn(2);
		ExpSimul eg=new ExpSimul(cfg);
		eg.setDuration(1000);
		TaskMng tm=eg.loadTM(0);
		tm.getInfo().setProb_ms(0.5); 
//		tm.prn();
		TaskSimul ts=new TaskSimul_EDF_VD();
		ts.set_tm(tm);
//		tm.prnInfo();

		SimulInfo si=eg.simul(ts);
		si.prn();
		return 1;
	}
	public int test2() 
	{
		ExpSimul eg=new ExpSimul();
		TaskMng tm=eg.loadTM("test/ts/taskset_0");
//		TaskMng tm=eg.loadTM("test/ts/taskset_1");
		eg.setDuration(1000);
		TaskSimul ts=new TaskSimul_EDF_VD(tm);

		SimulInfo si=eg.simul(ts);
		si.prn();
		return 1;
	}
	public int test3() 
	{
		return 0;
	}
	public  int test4() 
	{
		return 0;
	}
	public  int test5() 
	{
		return 0;
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
