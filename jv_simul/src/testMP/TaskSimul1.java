package testMP;

import simul.TaskSimul_EDF_AT_S;
import sysEx.TS_NonMC1;
import util.TEngine;

public class TaskSimul1 {
	public static int log_level=2;
//	public static int idx=-1;
	public static int idx=1;
	public static int gret[]={1,-1,-1,-1,-1, -1,-1,-1,-1,-1};


	public int test1()	{
		TaskSimul_EDF_AT_S ts=new TaskSimul_EDF_AT_S(TS_NonMC1.ts5());
		
		return ts.simulEnd(0,20);
	}
	public int test2() {
		return -1;
	}
	
	public  int test3()	{
		return -1;
	}
	
	public  int test4()	{
		return -1;
	}
	
	public  int test5() {
		return -1;
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
		Class c = TaskSimul1.class;
		TaskSimul1 m=new TaskSimul1();
		int[] aret=TaskSimul1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
