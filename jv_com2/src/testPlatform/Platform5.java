package testPlatform;


import util.MUtil;
import util.SEngineT;

public class Platform5 {
//	public static int idx=1;
//	public static int idx=2;
//	public static int idx=3;
	public static int idx=4;
//	public static int idx=-1;
	public static int log_level=3;
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
		Platform3 p=new Platform3(); // comp SCH
		p.isReal=0;
//		p.isReal=1;
		p.kind=0;
		p.test1();
		p.test2();
		p.test3();
		p.test4();
		p.kind=1;
		p.test1();
		p.test2();
		p.test3();
		return 0;
	}
	public  int test4() 
	{
		Platform4 p=new Platform4(); // comp DMR
		p.isReal=0;
//		p.isReal=1;
		p.test1();
		p.test2();
		p.test3();
		return 0;
	}
	public  int test5() 
	{
		test3();
		test4();
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
		Class c = Platform5.class;
		Platform5 m=new Platform5();
		int[] aret=Platform5.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
