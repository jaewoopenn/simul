package testComp;


import anal.AnalEDF_VD;
import comp.Comp;
import comp.CompMng;
import util.SLog;
import z_ex.CompMngEx1;
import util.SEngineT;

@SuppressWarnings("unused")
public class CompMng2 {
	public static int idx=3;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	public static int log_level=1;

	public int test1() 
	{
//		CompMng cm=CompMngEx1.getCompMng3();
//		TaskMng tm=cm.getTM();
//		double x=AnalEDF_VD.computeX(tm);
//		cm.setX(x);
//		cm.part();
//		cm.analMaxRes();
//		cm.prn();
//		Comp c=cm.getComp(0);
//		c.getTM().getTask(2).ms();
//		c.drop();
//		Log.prn(1, "RU:"+c.getRU());
//		c.getTM().prnStat();
//		cm.prnOff();
		return 0;
	}
	public int test2() 
	{
//		CompMng cm=CompMngEx1.getCompMng3();
//		TaskMng tm=cm.getTM();
//		double x=AnalEDF_VD.computeX(tm);
//		cm.setX(x);
//		cm.part();
//		cm.analMaxRes();
//		cm.prn();
//		Comp c=cm.getComp(0);
//		c.request(0.1);
//		Log.prn(1, "RU:"+c.getRU());
//		c.getTM().prnStat();
//		c.prnOff();
		return 0;
	}
	public int test3() 
	{
//		CompMng cm=CompMngEx1.getCompMng3();
//		TaskMng tm=cm.getTM();
//		double x=AnalEDF_VD.computeX(tm);
//		cm.setX(x);
//		cm.part();
//		cm.analMaxRes();
//		cm.prn();
//		Comp c=cm.getComp(0);
//		c.request(0.1);
//		Log.prn(1, "RU:"+c.getRU());
//		c.getTM().prnStat();
//		c.prnOff();
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
		Class c = CompMng2.class;
		CompMng2 m=new CompMng2();
		int[] aret=CompMng2.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}

}
