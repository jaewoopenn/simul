package Test;
import java.lang.reflect.Method;
import Test.TEngine;
import Util.Log;
public class Mock {
	public static int total=10;
	public static int gret[]={0,0,0,0,0,0,0,0,0,0};
	public int test1()
	{
		return 0;
	}
	public int test2()
	{
		return 0;
	}
	public  int test3()
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
		Class c = Mock.class;
		Mock m=new Mock();
		int[] aret=Mock.gret;
		int sz=Mock.total;
//		TEngine.run(m,c,aret,sz);
		TEngine.runOnce(m,c,aret,1,1);
	}

}
