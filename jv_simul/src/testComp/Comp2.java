package testComp;


import basic.TaskMng;
import comp.Comp;
import utilSim.Log;
import utilSim.TEngine;
import taskSetEx.CompEx1;

public class Comp2 {
	public static int idx=1;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	public static int log_level=1;

	public int test1() 
	{
		Comp c=CompEx1.getComp3();
		c.partition();
		c.prn();
		TaskMng tm=c.getTM();
		tm.setX(0.5);
		double ru=tm.getRU();
		Log.prn(1, "RU:"+ru);
		double u=c.getST_U();
		Log.prn(1, "init:"+u);
		u=c.getExt_U();
		Log.prn(1, "ext:"+u);
		u=c.getInt_U();
		Log.prn(1, "int:"+u);
		return 0;
	}
	public int test2() 
	{
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
		Class c = Comp2.class;
		Comp2 m=new Comp2();
		int[] aret=Comp2.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
