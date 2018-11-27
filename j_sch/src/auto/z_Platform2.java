package auto;


import anal.Anal;
import anal.AnalSel;
import basic.TaskMng;
import sim.SysMng;
import sim.TaskSimul_EDF_VD;
import gen.SysLoad;
import util.Log;
import util.TEngine;
import z_ex.TS_MC1;

public class z_Platform2 {
	public static int idx=1;
	public static int log_level=1;

	public int test1() 
	{
//		SysLoad sy=new SysLoad("test/t1/taskset_65");
//		sy.open();
//		TaskMng tm=sy.loadOne();
		TaskMng tm=TS_MC1.ts1();
		tm.prnInfo();
		Anal a=AnalSel.getAnal(0);
		a.init(tm);
		a.prepare();
		double x=a.computeX();
		Log.prn(1, "x:"+x);
		tm.setX(x);
		SysMng sm=new SysMng();
		sm.setMS_Prob(0.3);
		TaskSimul_EDF_VD tsim=new TaskSimul_EDF_VD(null);
		tsim.init_sm(sm);
		tsim.init_tm(tm);
		tsim.checkErr();
		tsim.simul(0,20);		
		return -1;		

	}

	public int test2() 
	{
		return -1;
	}
	
	public int test3() 
	{
		return -1;
	}
	
	public  int test4() 
	{
		return -1;
	}
	
	public  int test5() 
	{
		return -1;
	}
	public  int test6() 
	{
		return -1;
	}
	
	public  int test7()
	{
		return -1;
	}
	public  int test8()
	{
		return -1;
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
		Class c = z_Platform2.class;
		z_Platform2 m=new z_Platform2();
		int[] aret=z_Platform2.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
