package Test;

import java.lang.reflect.Method;

import Util.Log;

public class TEngine {
	public static void run(Object m,Class c,int[] aret, int sz) throws Exception 
	{
		for(int i=0;i<sz;i++)
		{
			Method meth = c.getMethod("test"+(i+1));
			int ret=(int)meth.invoke(m);
//			Log.prn(ret);
			Log.prnc(9,"Test "+(i+1));
			int sret=aret[i];
			if(ret==sret)
				Log.prn(9," OK");
			else
				Log.prn(9," Err "+ret+" "+sret);
		}
		
	}
	public static void runOnce(Object m, Class c, int[] aret, int idx,int lv) throws Exception {
		Log.set_lv(lv);
		Method meth = c.getMethod("test"+idx);
		int ret=(int)meth.invoke(m);
//			Log.prn(ret);
		Log.prnc(9,"Test "+idx);
		int sret=aret[idx-1];
		if(ret==sret)
			Log.prn(9," OK");
		else
			Log.prn(9," Err "+ret+" "+sret);
		
	}
	public static void main(String[] args) throws Exception {
		Class c = Mock.class;
		Mock mock=new Mock();
		int[] aret=Mock.gret;
		int sz=Mock.total;
//		run(mock,c,aret,sz);
		runOnce(mock,c,aret,1,1);
	}

}
