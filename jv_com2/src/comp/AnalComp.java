package comp;

import util.Log;
import util.MUtil;
import anal.AnalEDF_VD;
import task.TaskMng;

public class AnalComp {
	protected CompMng g_cm;
	public AnalComp(CompMng mng) {
		g_cm=mng;
	}
	public void computeX() {
		TaskMng tm=g_cm.getTM();
		AnalEDF_VD a=new AnalEDF_VD();
		a.init(tm);
		a.prepare();
		double x=a.getX();
		Log.prn(1, "sch:"+a.is_sch());
		g_cm.setX(x);
//		tm.setX(x);
	}
	public void part() {
		g_cm.part();
		
	}
	public int anal(int kinds) {
		int size=g_cm.getSize();
		double init_u=0;
		double wc_u=0;
		for(int i=0;i<size;i++){
			Comp c=g_cm.getComp(i);
			init_u+=c.getST_U();
			if(kinds==0)
				wc_u+=c.getWC_U();
			else
				wc_u+=c.getNa_U();
		}
		Log.prn(2, "initU:"+init_u);
		Log.prn(2, "wcU:"+wc_u);
		if(init_u>1+MUtil.err) return 0;
		if(wc_u>1+MUtil.err) return 0;
		return 1;
	}

}
