package testComp;


import comp.Comp;
import util.SLog;
import z_ex.CompEx1;
import util.SEngineT;

public class Comp1 {
	public static int idx=6;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	public static int log_level=1;

	public int test1() 
	{
		Comp c=CompEx1.getComp1();
		c.prn();
		return 0;
	}
	public int test2() 
	{
		Comp c=CompEx1.getComp2();
		c.prn();
		return 0;
	}
	public int test3() 
	{
		Comp c=CompEx1.getComp3();
		c.partition();
		c.prn();
		return 0;
	}

	// runtime util
	public  int test4() 
	{
		Comp c=CompEx1.getComp3();
		double ru=c.getTM().getRUtil();
		SLog.prn(1, "RU:"+ru);
		return 0;
	}

	// test max Res when ms
	public  int test5() 
	{
//		Comp c=CompEx1.getComp3();
//		c.setMaxRes(0.7);
//		c.prn();
//		TaskMng tm=c.getTM();
//		double ru=tm.getRU();
//		Log.prn(1, "RU:"+ru);
//		tm.getTask(4).ms();
//		ru=tm.getRU();
//		Log.prn(1, "RU:"+ru);
//		c.drop();
//		ru=tm.getRU();
//		Log.prn(1, "RU:"+ru);
//		tm.prnStat();
		return 0;
	}

	// test ext ms
	public  int test6() 
	{
//		Comp c=CompEx1.getComp3();
//		c.setMaxRes(0.7);
//		c.prn();
//		Log.prn(1, "RU:"+c.getRU());
//		c.request(0.1);
//		Log.prn(1, "RU:"+c.getRU());
////		c.prnStat();
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
		Class c = Comp1.class;
		Comp1 m=new Comp1();
		int[] aret=Comp1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}

}
