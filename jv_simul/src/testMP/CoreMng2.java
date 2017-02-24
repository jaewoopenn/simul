package testMP;
//move 


import anal.AnalMP;
import exp.ExpSimulMP;
import part.CoreMng;
import sysEx.TS_MP1;
import util.TEngine;

public class CoreMng2 {
	public static int idx=2;
	public static int log_level=2;


	public int test1()	{
		CoreMng cm=TS_MP1.core1();
		cm.prn();
		cm.move(null,0);
		cm.prn();
		return 0;
	}
	
	public int test2() {
		CoreMng cm=TS_MP1.core3();
//		cm.prn();
		int cpus=2;
		ExpSimulMP eg=new ExpSimulMP();
		
		eg.initCores(cpus);
		eg.loadCM(cm,new AnalMP(),3);
		eg.check();
//		eg.simul(0,300);
//		eg.prn();
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
