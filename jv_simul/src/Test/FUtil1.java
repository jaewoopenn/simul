package Test;

import Util.FUtil;
import Util.TEngine;

public class FUtil1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=1;
	public static int total=10;
	public static int gret[]={1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	public int test1() 
	{
		FUtil fu=new FUtil("test.txt");
		fu.print("hihi");
		fu.print("hihi!!");
		fu.print("hihi!11s!");
		fu.end();
		
		return 1;
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
		Class c = FUtil1.class;
		FUtil1 m=new FUtil1();
		int[] aret=FUtil1.gret;
		int sz=FUtil1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
