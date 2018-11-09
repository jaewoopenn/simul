package anal;

import basic.TaskMng;
import z_ex.TS_MC1;
import util.TEngine;

public class z_Anal1 {
	public static int idx=1;
	public static int log_level=1;

	public int test1() 
	{
		TaskMng tm=TS_MC1.ts1();
		Anal a=new AnalEDF();
		a.init(tm);
		a.prepare();
		a.prn();
		a=new AnalEDF_VD();
		a.init(tm);
		a.prepare();
		a.prn();
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
		Class c = z_Anal1.class;
		z_Anal1 m=new z_Anal1();
		int[] aret=z_Anal1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
