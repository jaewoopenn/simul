package com;


import util.SLog;
import util.MCal;
import util.SEngineT;

public class z_sbf1 {
	public static int log_level=1;
	public static int idx=1;
	public static int total=10;
	public static int gret[]={2,23,0,0,0,0,0,0,0,0};
	public int test1()
	{
		PRM p=new PRM(3,1.5);
		
		for(int t=0;t<12;t++) {
			double r=p.sbf(t);
			SLog.prnc(1, "t: "+t);
			SLog.prn(1, " r: "+MCal.getStr(r));
		}
		return 0;
	}
	public int test2() 
	{
		return 0;
	}
	public  int test3()
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
		Class c = z_sbf1.class;
		z_sbf1 m=new z_sbf1();
		int[] aret=z_sbf1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}

}
