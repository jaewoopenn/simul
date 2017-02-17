package File;

import util.FUtil;
import util.Log;
import util.TEngine;

public class FUtil1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=3;
	public static int gret[]={1,1,-1,-1,-1, -1,-1,-1,-1,-1};
	public int test1() 
	{
		FUtil fu=new FUtil("test.txt");
		fu.print("hihi");
		fu.print("hihi!!");
		fu.print("hihi!11s!");
		fu.save();
		
		return 1;
	}
	public int test2() 
	{
		FUtil fu=new FUtil("test.txt");
		fu.load();
		int no=fu.size();
		for(int i=0;i<no;i++)
			Log.prn(1, fu.get(i));
		return 1;
	}
	public int test3() 
	{
		String set="com";
//		String set="exp";
		FUtil.makeDir(set);
		FUtil.makeDir(set+"/cfg");
		FUtil.makeDir(set+"/ts");
		FUtil.makeDir(set+"/rs");
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
		Class c = FUtil1.class;
		FUtil1 m=new FUtil1();
		int[] aret=FUtil1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
