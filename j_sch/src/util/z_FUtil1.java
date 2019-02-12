package util;

public class z_FUtil1 {
	public static int idx=3;
	public static int log_level=1;
	public static int total=10;
	public static int gret[]={1,0,0,1,1, 1,0,0,0,0};
	public int test1() // gen
	{
		FUtilSp f=new FUtilSp("test\\t\\taskset");
		f.load();
		return 1;

	}
	public int test2() // load
	{
		FUtil f=new FUtil("test\\t\\taskset");
		f.br_open();
		String s=f.read();
		S_Log.prn(1, s);
		f.br_close();
		return 0;
	}
	public int test3() // load one
	{
		FUtil f=new FUtil("test\\t\\taskset");
		f.br_open();
		f.readSplit("------");
		f.view();
		S_Log.prn(1, "----------");
		
		f.readSplit("------");
		f.view();
		f.br_close();
		return 0;
	}
	public  int test4() // load copy\
	{
		return 0;
	}
	public  int test5() //
	{
		return 0;
	}
	public  int test6() // 
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
		Class c = z_FUtil1.class;
		z_FUtil1 m=new z_FUtil1();
		int[] aret=z_FUtil1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
