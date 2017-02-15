package util;

import java.lang.reflect.Method;

public class TEngine {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void run(Object m,Class c,int[] aret, int sz) throws Exception 
	{
		for(int i=0;i<sz;i++)
		{
			Method meth = c.getMethod("test"+(i+1));
			int ret=(int)meth.invoke(m);
//			Log.prn(ret);
			Log.prnc(9,"Test "+(i+1));
			int sret=aret[i];
			if(sret==-1)
				Log.prn(9, " Not used");
			else if(ret==sret)
				Log.prn(9," OK");
			else
				Log.prn(9," Err "+ret+" "+sret);
		}
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void runOnce(Object m, Class c, int[] aret, int idx,int lv) throws Exception {
		Log.set_lv(lv);
		Method meth = c.getMethod("test"+idx);
		int ret=(int)meth.invoke(m);
//			Log.prn(ret);
		Log.prnc(9,"Test "+idx);
		int sret=aret[idx-1];
		if(sret==-1)
			Log.prn(9, " Not used");
		else if(ret==sret)
			Log.prn(9," OK");
		else
			Log.prn(9," Err, result:"+ret+" expected:"+sret);
		
	}

}
