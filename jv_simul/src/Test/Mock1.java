package Test;

import Util.TEngine;

public class Mock1 {
	public static int log_level=1;
	public static int idx=-1;
//	public static int idx=1;
	public static int total=10;
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	public int test1() 
	{
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
	public static void main(String[] args) throws Exception {
		Class c = Mock1.class;
		Mock1 m=new Mock1();
		int[] aret=Mock1.gret;
		int sz=Mock1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}