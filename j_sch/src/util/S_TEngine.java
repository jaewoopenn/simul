/*
 * Static Class
 */
package util;

import java.lang.reflect.Method;

public class S_TEngine {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void run(Object m,Class c,int[] aret, int sz) throws Exception 
	{
		for(int i=0;i<sz;i++)
		{
			Method meth = c.getMethod("test"+(i+1));
			int ret=(int)meth.invoke(m);
//			Log.prn(ret);
			S_Log.prnc(9,"Test "+(i+1));
			int sret=aret[i];
			if(sret==-1)
				S_Log.prn(9, " Not used");
			else if(ret==sret)
				S_Log.prn(9," OK");
			else
				S_Log.prn(9," Err "+ret+" "+sret);
		}
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void runOnce(Object m, Class c, int[] aret, int idx,int lv) throws Exception {
		S_Log.set_lv(lv);
		Method meth = c.getMethod("test"+idx);
		int ret=(int)meth.invoke(m);
//			Log.prn(ret);
		S_Log.prnc(9,"Test "+idx);
		int sret=aret[idx-1];
		if(sret==-1)
			S_Log.prn(9, " Not used");
		else if(ret==sret)
			S_Log.prn(9," OK");
		else
			S_Log.prn(9," Err, result:"+ret+" expected:"+sret);
		
	}

}