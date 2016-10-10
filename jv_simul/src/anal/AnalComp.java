package anal;

import utilSim.Log;
import basic.TaskMng;
import comp.Comp;
import comp.CompMng;

public class AnalComp {
	protected CompMng g_cm;
	protected int g_limit=10000;
	public AnalComp(CompMng mng) {
		g_cm=mng;
	}
	public void computeX() {
		TaskMng tm=g_cm.getTM();
		AnalEDF_AT_S a=new AnalEDF_AT_S();
		a.init(tm);
		a.prepare();
		double x=a.getX();
		Log.prn(1, "sch"+a.isScheduable());
		tm.setX(x);
	}
	public void part() {
		g_cm.part();
		
	}
	public int anal() {
		int size=g_cm.getSize();
		double init_u=0;
		double wc_u=0;
		for(int i=0;i<size;i++){
			Comp c=g_cm.getComp(i);
			init_u+=c.getInitU();
			wc_u+=Math.max(c.getExtU(), c.getIntU());
		}
		Log.prn(2, "initU:"+init_u);
		Log.prn(2, "wcU:"+wc_u);
		if(init_u>1) return 0;
		if(wc_u>1) return 0;
		return 1;
	}

}
