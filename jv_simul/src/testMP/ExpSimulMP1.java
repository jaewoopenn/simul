package testMP;
import anal.Anal;
import anal.AnalEDF_VD;
import basic.TaskMng;
import basic.TaskSetFix;
import exp.ExpSimulMP;
import exp.ExpSimulTM;
import gen.ConfigGen;
import part.Partition;
import simul.SimulInfo;
import simul.TaskSimul_EDF_VD;
import sysEx.TS_MP1;
import util.MUtil;
import util.TEngine;

public class ExpSimulMP1 {
	public static int idx=4;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	public static int log_level=2;
	public int test1() 
	{
		TaskMng tm=TaskMng.getFile("test/ts/taskset_0");
		
		ExpSimulMP eg=new ExpSimulMP(1);
		eg.initSim(0,new TaskSimul_EDF_VD(tm));
		eg.simul(0,1000);
		eg.prn();
		return 1;
	}
	public int test2() 
	{
		TaskMng tm=TaskMng.getFile("test/ts/taskset_0");
		ExpSimulMP eg=new ExpSimulMP(2);
		eg.initSim(0,new TaskSimul_EDF_VD(tm));
		tm=TaskMng.getFile("test/ts/taskset_2");
		eg.initSim(1,new TaskSimul_EDF_VD(tm));
		eg.simul(0,1000);
		eg.prn();
		return 1;
	}
	public int test3() 
	{
		ExpSimulMP eg=new ExpSimulMP(2);
		TaskMng tm=TaskMng.getFile("test/ts/taskset_0");
		eg.initSim(0,new TaskSimul_EDF_VD(tm));
		tm=TaskMng.getFile("test/ts/taskset_2");
		eg.initSim(1,new TaskSimul_EDF_VD(tm));
		eg.simul(0,100);
		eg.prn();
		return 1;
	}
	public  int test4() 
	{
		TaskMng tm=TS_MP1.ts3();
		Anal an=new AnalEDF_VD();
		Partition p=new Partition(an,tm.getTaskSet());
		p.anal();
		int ncpu=p.size();
//		p.getCoreMng();
		
//		p.prn();
		ExpSimulMP eg=new ExpSimulMP(ncpu);
		for(int i:MUtil.loop(ncpu)){
			TaskSetFix tsf=new TaskSetFix(p.getTS(i));
			tm=tsf.getTM();
			an.init(tm);
			an.prepare();
			tm.setX(an.getX());
			eg.initSim(i,new TaskSimul_EDF_VD(tm));
		}
		eg.simul(0,1000);
		eg.prn();
		
		return 1;
	}
	public  int test5() 
	{
		ExpSimulMP eg=new ExpSimulMP(2);
		TaskMng tm=TaskMng.getFile("test/ts/mp_0");
		Partition p=new Partition(new AnalEDF_VD(),tm.getTaskSet());
		p.anal();
		p.prn();

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
		Class c = ExpSimulMP1.class;
		ExpSimulMP1 m=new ExpSimulMP1();
		int[] aret=ExpSimulMP1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
