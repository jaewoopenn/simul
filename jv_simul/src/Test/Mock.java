package Test;
import java.lang.reflect.Method;

import Util.Log;
public class Mock {
	public static int ret[]={0,1,0};
	public int test1()
	{
		System.out.println("hihi");
		return 0;
	}
	public int test2()
	{
//		System.out.println("hihi");
		return 0;
	}
	public  int test3()
	{
//		System.out.println("hihi");
		return 1;
	}
	public void run()
	{
		if(test1()!=ret[0]) Log.prn("Error 1");
		if(test2()!=ret[1]) Log.prn("Error 2");
		if(test3()!=ret[2]) Log.prn("Error 3");
		Log.prn("End");
	}
	public static void main(String[] args) throws Exception {
		Mock mock=new Mock();
		Class c = Mock.class;
		for(int i=0;i<3;i++)
		{
			Method meth = c.getMethod("test"+(i+1));
			int ret=(int)meth.invoke(mock);
			Log.prn(ret);
			if(ret==Mock.ret[i])
				Log.prn("OK");
			else
				Log.prn("Err");
		}
//		mock.run();
	}

}
