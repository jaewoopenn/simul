package comp;

import util.SLog;
import util.MUtil;
import anal.Anal;
import anal.AnalEDF_AD_E;
import anal.AnalEDF_VD;
import task.TaskMng;

public class AnalComp {
	protected CompMng g_cm;
	public AnalComp(CompMng mng) {
		g_cm=mng;
	}
	public void computeX() {
		TaskMng tm=g_cm.getTM();
		Anal a=new AnalEDF_VD();
		a.init(tm);
		a.prepare();
		double x=a.computeX();
		SLog.prn(1, "sch:"+a.is_sch());
		SLog.prn(1, "x:"+x);
		g_cm.setX(x);
		g_cm.prn();
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
			if(kinds==0) // FCS
				wc_u+=c.getWC_U();
			else // Naive
				wc_u+=c.getNa_U();
		}
		SLog.prn(2, "initU:"+init_u);
		SLog.prn(2, "wcU:"+wc_u);
		if(init_u>1+MUtil.err) return 0;
		if(wc_u>1+MUtil.err) return 0;
		return 1;
	}

}
