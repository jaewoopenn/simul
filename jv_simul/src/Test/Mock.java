package Test;
import java.lang.reflect.Method;

import Util.Log;
public class Mock {
	public static int total=10;
	public static int ret[]={0,0,0,0,0,0,0,0,0,0};
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
		Mock mock=new Mock();
		int[] aret=Mock.ret;
		int sz=Mock.total;
		for(int i=0;i<sz;i++)
		{
			Method meth = c.getMethod("test"+(i+1));
			int ret=(int)meth.invoke(mock);
//			Log.prn(ret);
			Log.prnc("Test "+(i+1));
			if(ret==aret[i])
				Log.prn(" OK");
			else
				Log.prn(" Err "+ret+" "+Mock.ret[i]);
		}
//		mock.run();
	}

}
