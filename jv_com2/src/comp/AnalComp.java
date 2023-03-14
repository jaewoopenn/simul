package comp;

import util.SLog;
import util.MUtil;
import anal.Anal;
import anal.AnalEDF_AD_E;
import anal.AnalEDF_AD_E2;
import anal.AnalEDF_VD;
import task.TaskMng;

public class AnalComp {
	protected CompMng g_cm;
	public AnalComp(CompMng mng) {
		g_cm=mng;
	}
	public boolean computeX(int kinds) {
		TaskMng tm=g_cm.getTM();
		Anal a;
		double x=-1;
		if(kinds==2) {
			a=new AnalEDF_VD();
			a.init(tm);
			x=a.computeX();
			g_cm.setX2(x);
		}
		else {
//			a=new AnalEDF_VD();
//			a=new AnalEDF_AD_E();
			a=new AnalEDF_AD_E2();
			a.init(tm);
			x=a.computeX();
			g_cm.setX(x);
		}
		SLog.prn(1, "sch:"+a.is_sch());
		SLog.prn(1, "x:"+x);
//		g_cm.prn();
		return a.is_sch();
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
			else if(kinds==1) // MC-ADAPT
				wc_u+=c.get_isol_U();
			else  // EDF-VD
				wc_u+=Math.max(c.getST_U2(),c.get_isol_U());
		}
		SLog.prn(2, "initU:"+init_u);
		SLog.prn(2, "wcU:"+wc_u);
		if(init_u>1+MUtil.err) return 0;
		if(wc_u>1+MUtil.err) return 0;
		return 1;
	}

}
