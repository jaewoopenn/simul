package com;


import util.SLog;
import util.MCal;
import util.SEngineT;

public class z_sbf2 {
	public static int log_level=1;
	public static int idx=1;
	public static int total=10;
	public static int gret[]={2,23,0,0,0,0,0,0,0,0};
	public int test1()
	{
		PRM p=new PRM(3,1.3);
		double r=0;
		SLog.prn(1, "xxx nSBF iSBF");
		for(int t=0;t<20;t++) {
			SLog.prnc(1, ""+t);
//			r=p.sbf(t);
//			SLog.prnc(1, " "+MCal.getStr(r));
			r=p.sbf_n(t);
			SLog.prnc(1, " "+MCal.getStr(r));
			r=p.sbf_i(t);
			SLog.prn(1, " "+MCal.getStr(r));
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
		Class c = z_sbf2.class;
		z_sbf2 m=new z_sbf2();
		int[] aret=z_sbf2.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}

}
