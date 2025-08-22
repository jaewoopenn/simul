package task;
/*
 * print task 
 */

import util.MCal;
import util.SLog;

public class PrnTask {
	public static void prnStat(Task t) {
		SLog.prnc(2, "tid:"+t.tid);
		SLog.prnc(2, ", "+MCal.getStr(t.getLoUtil()));
		SLog.prnc(2, ", "+MCal.getStr(t.getHiUtil()));
		if (t.isHC()){
			SLog.prnc(2," isHM:"+t.isHM());
			SLog.prn(2,", is_hi_preferred:"+t.isHI_Preferred());
			
		}else{
			SLog.prn(2," isDrop:"+t.isDrop());
		}
		
	}
	public static void prnPara(Task t) {
		SLog.prnc(2, "tmp.add(new Task(0,");
		SLog.prnc(2, t.period);
		if (t.isHC()){
			SLog.prnc(2, ", "+t.c_l);
			SLog.prnc(2, ", "+t.c_h);
		} else {
			SLog.prnc(2, ", "+t.c_l);
		}
		SLog.prn(2, "));");

	}		
}

