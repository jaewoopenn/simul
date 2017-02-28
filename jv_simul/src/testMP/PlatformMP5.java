package testMP;


import util.Log;
import util.MUtil;
import util.TEngine;

public class PlatformMP5 {
	public static int idx=3;
//	public static int idx=-1;
	public static int log_level=3;
	public int test1() 
	{
//		int isReal=0;
		int isReal=1;
		Log.prn(9, "test1 "+isReal);
		PlatformMP3 p=new PlatformMP3(); // TM DMR
		p.isReal=isReal;
		p.test1();
		p.test2();
		p.test3();
		MUtil.sendMail("DMR anal 1 OK");

		return 0;
	}
	public int test2() 
	{
//		int isReal=0;
		int isReal=1;
		Log.prn(9, "test2 "+isReal);
		PlatformMP4 p2=new PlatformMP4(); // TM DMR
		p2.isReal=isReal;
		p2.test1();
		p2.test2();
		p2.test3();
		MUtil.sendMail("DMR anal 2 OK");
		return 0;
	}
	public int test3() 
	{
//		int isReal=0;
		int isReal=1;
		Log.prn(9, "test3 "+isReal);
		PlatformMP3 p=new PlatformMP3(); // TM DMR
		p.isReal=isReal;
		p.test1();
		p.test2();
		p.test3();

		PlatformMP4 p2=new PlatformMP4(); // TM DMR
		p2.isReal=isReal;
		p2.test1();
		p2.test2();
		p2.test3();
		MUtil.sendMail("DMR anal 3 OK");
		return 0;
	}
	public  int test4() 
	{
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
		Class c = PlatformMP5.class;
		PlatformMP5 m=new PlatformMP5();
		int[] aret=PlatformMP5.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
