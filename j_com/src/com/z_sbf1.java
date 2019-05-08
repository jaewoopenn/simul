package com;


import util.S_Log;
import util.S_TEngine;

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
			S_Log.prnc(1, "t: "+t);
			S_Log.prn(1, " r: "+r);
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
			S_TEngine.run(m,c,aret,10);
		else
			S_TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
