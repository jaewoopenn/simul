package Test;

import java.lang.reflect.Method;

import Util.Log;

public class TestEngine {
	public static void run(Object mock,Class c,int[] aret, int sz) throws Exception 
	{
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
		
	}
	public static void main(String[] args) throws Exception {
		Class c = Mock.class;
		Mock mock=new Mock();
		int[] aret=Mock.ret;
		int sz=Mock.total;
		run(mock,c,aret,sz);
	}

}
