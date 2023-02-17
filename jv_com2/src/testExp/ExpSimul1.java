package testExp;
import anal.Anal;
import anal.AnalEDF_VD;
import exp.ExpSimulTM;
import gen.ConfigGen;
import sim.SimulInfo;
import sim.mc.TaskSimul_EDF_VD;
import task.TaskSetMC;
import task.TaskSetUtil;
import util.MList;
import util.SEngineT;

public class ExpSimul1 {
	public static int idx=1;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	public static int log_level=2;
	public int test1() 
	{
		ConfigGen cfg=new ConfigGen("fc/cfg/cfg_util_55.txt");
		cfg.readFile();
		ExpSimulTM eg=new ExpSimulTM(cfg);
		String fn=cfg.get_fn(0);
		TaskSetMC tm=TaskSetUtil.loadFile(new MList(fn));
		Anal an=new AnalEDF_VD();
		an.init(tm.getTM());
		an.prepare();
		eg.initSim(0, new TaskSimul_EDF_VD());
		eg.simul(0,1000);
		SimulInfo si=eg.getSI(0);

		si.prn();
		return 1;
	}
	public int test2() 
	{
//		ExpSimulTM eg=new ExpSimulTM(null);
//		TaskMng tm=TaskMng.getFile("test/ts/taskset_0");
////		TaskMng tm=TaskMng.getFile("test/ts/taskset_1");
//		
//		Anal an=new AnalEDF_VD();
//		an.init(tm);
//		an.prepare();
//		tm.setX(an.computeX());
//
//		eg.initSim(0, new TaskSimul_EDF_VD(tm));
//		eg.simul(0,100);
//		SimulInfo si=eg.getSI(0);
//		si.prn();
		return 1;
	}
	public int test3() 
	{
		return 1;
	}
	public  int test4() 
	{
		return 1;
	}
	public  int test5() 
	{
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
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}

}
