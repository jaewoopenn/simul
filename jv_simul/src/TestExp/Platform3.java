package TestExp;

import Util.Log;
import Util.TEngine;
import Exp.Platform;

public class Platform3 {
//	public static int idx=-1;
	public static int idx=2;
	public static int total=10;
	public static int gret[]={0,0,0,0,0, 1,0,1,1,1};
	
	public int test1()
	{
		Platform p=new Platform();
		p.addTask(3,2);
		p.addTask(4,1);
		return 	p.post(12);
	}

	public int test2() // error 
	{
		Platform p=new Platform();
		p.addHiTask(6,1,5);
		p.addTask(4,2);
		p.setMS(2);
		return 	p.post(12);
	}

	public int test3() // vd set
	{
		Platform p=new Platform();
		p.addHiTask(6,1,5);
		p.addTask(4,2);
		p.setMS(2);
		return 	p.post(12);
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

	public static void main(String[] args) throws Exception {
		Class c = Platform3.class;
		Platform3 m=new Platform3();
		int[] aret=Platform3.gret;
		int sz=Platform3.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,1);
	}

}
