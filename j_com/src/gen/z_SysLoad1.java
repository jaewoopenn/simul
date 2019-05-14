package gen;
import basic.TaskMng;
import util.S_TEngine;

public class z_SysLoad1 {
//	public static int idx=1;
//	public static int idx=2;
	public static int idx=3;
	public static int log_level=1;
	public int test1() // gen
	{
		SysLoad sy=new SysLoad("com/test1");
		sy.load();

		return 1;

	}
	public int test2() // load
	{
		SysLoad sy=new SysLoad("com/test1");
		sy.open();
		TaskMng tm=sy.loadOne();
		tm.prn();
		return 0;
	}
	public int test3() // load one
	{
		SysLoad sy=new SysLoad("com/test1");
		sy.open();
		TaskMng tm;
		while((tm=sy.loadOne())!=null) {
			tm.prnInfo();
		}

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
		Class c = z_SysLoad1.class;
		z_SysLoad1 m=new z_SysLoad1();
		int[] aret=z_SysLoad1.gret;
		if(idx==-1)
			S_TEngine.run(m,c,aret,10);
		else
			S_TEngine.runOnce(m,c,aret,idx,log_level);
	}
	public static int total=10;
	public static int gret[]={1,0,0,1,1, 1,0,0,0,0};

}
