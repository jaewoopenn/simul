package util;

public class z_FLog {
	public static int idx=3;
	public static int log_level=1;
	public static int total=10;
	public static int gret[]={1,0,0,1,1, 1,0,0,0,0};
	public int test1() 
	{
		FLog.init("test/log.txt");
		FLog.prn("hihi");
		FLog.prn("hihi2");
		FLog.end();
		return 1;

	}
	public int test2()  //err
	{
		FLog.prn("hihi");
		FLog.prn("hihi2");
		FLog.end();
		return 0;
	}
	public int test3() 
	{
		FOut f=new FOut("test/out.txt");
		f.write("hihi");
		f.write("hihi2");
		f.save();
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
		Class c = z_FLog.class;
		z_FLog m=new z_FLog();
		int[] aret=z_FLog.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
