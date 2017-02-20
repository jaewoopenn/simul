package testMP;


import anal.Anal;
import anal.AnalEDF_VD;
import basic.TaskMng;
import exp.ExpSimulMP;
// move 
import part.CoreMng;
import simul.TaskSimul_EDF_VD;
import sysEx.TS_MP1;
import util.MUtil;
import util.TEngine;

public class CoreMng2 {
	public static int idx=2;
	public static int log_level=2;


	public int test1()	{
		CoreMng cm=TS_MP1.core1();
		cm.prn();
		cm.move();
		cm.prn();
		return 0;
	}
	public int test2() {
		
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
		eg.simul(0,500);
		eg.move();
		eg.simul(500,1000);
		eg.prn();
		
		return 0;
	}
	
	public  int test3()	{
		return 0;
		
	}
	
	public  int test4()	{
		return 0;
	}
	
	public  int test5() {
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
