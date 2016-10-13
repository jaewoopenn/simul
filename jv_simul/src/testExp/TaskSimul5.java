package testExp;

import anal.AnalEDF_VD;
import basic.TaskMng;
import comp.CompFile;
import comp.CompMng;
import exp.TaskSimul_FC_MCS;
import taskSetEx.CompMngEx1;
import taskSetEx.TS_MC1;
import utilSim.TEngine;

// Comp
public class TaskSimul5 {
	public static int idx=2;
//	public static int idx=-1;
	public static int log_level=1;
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};

	// Comp
	public int test1() 
	{
		CompMng cm=CompMngEx1.getCompMng3();
		TaskMng tm=cm.getTM();
		double x=AnalEDF_VD.computeX(tm);
		cm.setX(x);
		cm.part();
		cm.analMaxRes();
		tm.prnComp();
		tm.getInfo().setProb_ms(1.0);
		TaskSimul_FC_MCS ts=new TaskSimul_FC_MCS(tm);
		ts.set_cm(cm);
		ts.simulEnd(0,20);
		return 0;
	}
	public int test2() 
	{
		String f="com/ts/taskset_util_85_19";
		CompMng cm=CompFile.loadFile(f);
		TaskMng tm=cm.getTM();
		double x=AnalEDF_VD.computeX(tm);
		cm.setX(x);
		cm.part();
		cm.analMaxRes();
		tm.prnComp();
		tm.getInfo().setProb_ms(1);
		TaskSimul_FC_MCS ts=new TaskSimul_FC_MCS(tm);
		ts.set_cm(cm);
		ts.simulEnd(0,400);
		return 0;
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
		Class c = TaskSimul5.class;
		TaskSimul5 m=new TaskSimul5();
		int[] aret=TaskSimul5.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
