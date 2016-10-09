package testComp;


import comp.Comp;
import utilSim.TEngine;
import taskSetEx.CompEx1;

public class Comp1 {
	public static int idx=3;
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
		Class c = Comp1.class;
		Comp1 m=new Comp1();
		int[] aret=Comp1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
