package Test;

import Simul.Script;
import Util.TEngine;

public class Script1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=2;
	public static int total=10;
	public static int gret[]={1,1,-1,-1,-1, -1,-1,-1,-1,-1};
	public int test1() 
	{
		Script s=new Script();
		s.test();
		return 1;
	}
	public int test2() 
	{
		Script s=new Script();
		s.load("scr/test.txt");
		return 1;
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
	public static void main(String[] args) throws Exception {
		Class c = Script1.class;
		Script1 m=new Script1();
		int[] aret=Script1.gret;
		int sz=Script1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}