package testMP;

import anal.Anal;
import anal.AnalEDF_VD;
import basic.TaskMng;
import basic.TaskSetFix;
import exp.ExpSimulMP;
import part.CoreMng;
import part.Partition;
import simul.TaskSimul_EDF_VD;
import sysEx.TS_MP1;
import util.Log;
import util.MUtil;
import util.TEngine;

public class CoreMng1 {
	public static int idx=5;
	public static int log_level=2;


	public int test1() {
		CoreMng cm=TS_MP1.core2();
		TaskSimul_EDF_VD ts=new TaskSimul_EDF_VD(cm.getTM(0));
		Log.set_lv(1);
		ts.simulBy(0,20);
		ts.simulEnd(20);
		return 0;
	}
	
	public  int test2()	{
		CoreMng cm=TS_MP1.core2();
		TaskSimul_EDF_VD ts=new TaskSimul_EDF_VD(cm.getTM(0));
		Log.set_lv(1);
		ts.simulEnd(0,20);
		return 0;
		
	}
	
	public  int test3()	{
		
		CoreMng cm=TS_MP1.core3();
		int cpus=2;
		ExpSimulMP eg=new ExpSimulMP();
		eg.initCores(cpus);
		for(int i:MUtil.loop(cpus)){
			TaskMng tm=cm.getTM(i);
			Anal an=new AnalEDF_VD();
			an.init(tm);
			an.prepare();
			tm.setX(an.getX());
			
			eg.initSim(i,new TaskSimul_EDF_VD(tm));
		}
		eg.simul(0,100);
		eg.prn();
		
		return 0;
	}
	
	public  int test4() {
		TaskMng tm=TS_MP1.ts3();
		Anal an=new AnalEDF_VD();
		Partition p=new Partition(an,tm.getTaskSet());
		p.anal();
		CoreMng cm=p.getCoreMng();
		
//		p.prn();
		
		ExpSimulMP eg=new ExpSimulMP();
		eg.initCores(cm.size());
		for(int i:MUtil.loop(cm.size())){
			TaskSetFix tsf=new TaskSetFix(cm.getTS(i));
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
	
	public  int test5()	{
		TaskMng tm=TaskMng.getFile("test/ts/mp_0");
		Anal an=new AnalEDF_VD();
		Partition p=new Partition(an,tm.getTaskSet());
		p.anal();
		CoreMng cm=p.getCoreMng();
		
		ExpSimulMP eg=new ExpSimulMP();
		eg.initCores(cm.size());
		eg.loadCM(cm,an);
		eg.simul(0,1000);
		eg.prn();
		return -1;
	}
	public int test6()	{
		return 0;
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
		Class c = CoreMng1.class;
		CoreMng1 m=new CoreMng1();
		int[] aret=CoreMng1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
