package util;

public class z_FLog {
	public static int idx=3;
	public static int log_level=1;
	public static int total=10;
	public static int gret[]={1,0,0,1,1, 1,0,0,0,0};
	public int test1() 
	{
		S_FLog.init("test/log.txt");
		S_FLog.prn("hihi");
		S_FLog.prn("hihi2");
		S_FLog.end();
		return 1;

	}
	public int test2()  //err
	{
		S_FLog.prn("hihi");
		S_FLog.prn("hihi2");
		S_FLog.end();
		return 0;
	}
	public int test3() 
	{
		MList f=new MList();
		f.add("hihi");
		f.add("hihi2");
		f.save("test/out.txt");
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
			S_TEngine.run(m,c,aret,10);
		else
			S_TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
