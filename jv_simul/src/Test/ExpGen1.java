package Test;
import Test.TEngine;
import Simul.ExpGen;

public class ExpGen1 {
//	public static int idx=-1;
	public static int idx=5;
	public static int total=10;
	public static int gret[]={0,0,7,0,0,0,0,0,0,0};
	public int test1()
	{
		ExpGen eg=new ExpGen();
		return eg.readConfig("config/err_cfg1.txt");
	}
	public int test2()
	{
		ExpGen eg=new ExpGen();
		eg.readConfig("config/cfg1.txt");
		String s=eg.readPar("util_err");
		if(s==null) 
			return 0;
		System.out.println(s);
		return 1;
	}
	public  int test3()
	{
		ExpGen eg=new ExpGen();
		eg.readConfig("config/cfg1.txt");
		String s=eg.readPar("util");
//		System.out.println(s);
		return (int)(Double.valueOf(s).doubleValue()*10);
	}
	public  int test4()
	{
		ExpGen eg=new ExpGen();
		eg.readConfig("config/cfg1.txt");
		eg.gen();
		return 0;
	}
	public  int test5()
	{
		ExpGen eg=new ExpGen();
		eg.readConfig("config/cfg1.txt");
		eg.load();
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
		Class c = ExpGen1.class;
		ExpGen1 m=new ExpGen1();
		int[] aret=ExpGen1.gret;
		int sz=ExpGen1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,1);
	}

}
