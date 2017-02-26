package testMP;
//move 


import basic.TaskMng;
import anal.Anal;
import anal.AnalEDF_AD_E;
import anal.AnalMP;
import exp.ExpSimulMP;
import part.CoreMng;
import part.Partition;
import simul.SimulInfo;
import sysEx.TS_MP1;
import util.Log;
import util.MUtil;
import util.TEngine;

public class CoreMng2 {
	public static int idx=5;
	public static int log_level=2;


	public int test1()	{
		CoreMng cm=TS_MP1.core1();
		cm.prn();
		cm.move(cm.getTS(0).get(1),1);
		cm.prn();
		return 0;
	}
	
	public int test2() {
		CoreMng cm=TS_MP1.core3();
//		cm.prn();
		int cpus=2;
		ExpSimulMP eg=new ExpSimulMP();
		
		eg.initCores(cpus);
		eg.loadCM(cm,new AnalMP(),1);
//		eg.loadCM(cm,new AnalMP(),2);
		eg.check();
//		eg.simul(0,1000);
		eg.prnTasks();
		return 0;
	}
	
	public  int test3()	{
		ExpSimulMP eg=new ExpSimulMP();
		eg.initCores(2);
		Anal an=new AnalMP(); 
		
		String fn="mp/ts/util_sim_155/taskset_25";
		TaskMng tm=TaskMng.getFile(fn);
		Partition p=new Partition(an,tm.getTaskSet());
		p.anal();
		if(!p.check()){
			Log.prn(2, "Part ERR");
			return 0;
		}
		int cpu=p.size();
		Log.prn(2, "p cpus:"+cpu);
		CoreMng cm=p.getCoreMng();

		eg.loadCM(cm,an,1);
//		eg.loadCM(cm,an,2);
		eg.prnTasks();
		eg.check();
		if(eg.checkTasks())
			Log.prn(2, "Part OK");
		else
			Log.prn(2, "Part ERR");
		eg.simul(0,300);
		SimulInfo si=eg.getSI(0);
		Log.prn(2, si.getDMR()+","+si.ms+","+si.mig);
		si=eg.getSI(1);
		Log.prn(2, si.getDMR()+","+si.ms+","+si.mig);
			
		return 0;
		
	}
	
	public  int test4()	{
		ExpSimulMP eg=new ExpSimulMP();
		eg.initCores(2);
		Anal an=new AnalEDF_AD_E(); 
		int g_dur=100;
		for(int i:MUtil.loop(11,12)){
			String fn="mp/ts/util_sim_135/taskset_"+i;
			TaskMng tm=TaskMng.getFile(fn);
			Partition p=new Partition(an,tm.getTaskSet());
			p.anal();
			
			CoreMng cm=p.getCoreMng();
			eg.loadCM(cm,an,1);
			eg.check();
			eg.simul(0,g_dur);
			SimulInfo si=eg.getSI(0);
			Log.prn(2, i+","+si.getDMR()+","+si.ms+","+si.mig);
			si=eg.getSI(1);
			Log.prn(2, i+","+si.getDMR()+","+si.ms+","+si.mig);
		}
		
		return 0;
	}
	
	public  int test5() {
		ExpSimulMP eg=new ExpSimulMP();
		eg.initCores(2);
		Anal an=new AnalMP(); 
		String fn="mp/ts/util_sim_155/taskset_25";
		TaskMng tm=TaskMng.getFile(fn);
		Partition p=new Partition(an,tm.getTaskSet());
		p.anal();
		CoreMng cm=p.getCoreMng();

		eg.loadCM(cm,an,1);
		eg.prnTasks();
//		eg.simul(0,3000);
		
		return 0;
	}
	public  int test6()	{
		return -1;
	}
	public  int test7()	{
		return -1;
	}
	public  int test8()	{
		return -1;
	}
	public  int test9() {
		return -1;
	}
	public  int test10() {
		return -1;
	}



	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = CoreMng2.class;
		CoreMng2 m=new CoreMng2();
		int[] aret=CoreMng2.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
