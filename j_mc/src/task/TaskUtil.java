package task;

import util.MCal;
import util.SLog;

public class TaskUtil {
	
	public static void prn(Task t) {
		if(t.removed())
			SLog.prnc(2, "removed ");
		SLog.prnc(2, "ID: "+t.tid);
		SLog.prnc(2, " p:"+t.period);
		if (t.is_HC){
			if(t.is_hi_preferred) {
				SLog.prnc(2," HP cl:"+t.c_l+" ch:"+t.c_h);
			} else {
				SLog.prnc(2," HC cl:"+t.c_l+" ch:"+t.c_h+" vd:"+t.vd);
				SLog.prnc(2," mode:"+t.is_HI_Mode);
			}
		}else{
			SLog.prnc(2," LC ac:"+t.c_l);
			SLog.prnc(2," de:"+t.c_h);
			SLog.prnc(2," isDrop:"+t.is_dropped);
		}
		SLog.prn(2,"");
	}
	public static void prnShort(Task t) {
		SLog.prnc(2, t.tid);
		SLog.prnc(2, ", "+t.period);
		SLog.prnc(2, ", "+t.c_l);
		SLog.prnc(2, ", "+t.c_h);
		if (t.is_HC)
			SLog.prn(2,", H");
		else
			SLog.prn(2,", L");
	}
	public static void prnTxt(Task t)  {
		if(t.is_new())
			SLog.prnc(2, "new ");
		if(t.removed())
			SLog.prnc(2, "removed ");
		SLog.prnc(2, "ID: "+t.tid);
		SLog.prnc(2, ", p: "+t.period);
		SLog.prnc(2, ", cl: "+t.c_l);
		SLog.prnc(2, ", ch: "+t.c_h);
		if (t.is_HC)
			SLog.prn(2,", HC");
		else
			SLog.prn(2,", LC");
	}
	
	
	public static void prnRuntime(Task t)  {
		if(t.removed())
			SLog.prnc(2, "removed ");
		SLog.prnc(2, "tid:"+t.tid);
		SLog.prnc(2, ", "+MCal.getStr(t.getLoUtil()));
		SLog.prnc(2, ", "+MCal.getStr(t.getHiUtil()));
		if (t.is_HC){
			SLog.prn(2," isHM:"+t.is_HI_Mode);
//			SLog.prn(2," isHI_Only:"+is_hi_preferred);
			
		}else{
			SLog.prn(2," isDrop:"+t.is_dropped);
		}
		
	}
	public static void prnStat(Task t)  {
		if(t.removed())
			SLog.prnc(2, "removed ");
		SLog.prnc(2, "tid:"+t.tid);
		SLog.prnc(2, ", "+MCal.getStr(t.getLoUtil()));
		SLog.prnc(2, ", "+MCal.getStr(t.getHiUtil()));
		if (t.is_HC){
			SLog.prnc(2," isHM:"+t.is_HI_Mode);
			SLog.prn(2,", is_hi_preferred:"+t.is_hi_preferred);
			
		}else{
			SLog.prn(2," isDrop:"+t.is_dropped);
		}
		
	}
	
	public static void prnOffline(Task t)  {
		if(t.removed())
			SLog.prnc(2, "removed ");
		SLog.prnc(2, "ID: "+t.tid);
		SLog.prnc(2, ", p: "+t.period);
		SLog.prnc(2, ", vd: "+MCal.getStr(t.vd));
		SLog.prnc(2, ", cl: "+t.c_l);
		SLog.prnc(2, ", ch: "+t.c_h);
		
//		SLog.prnc(2, ", "+MCal.getStr(t.getLoUtil()));
//		SLog.prnc(2, ", "+MCal.getStr(t.getHiUtil()));
		if (t.is_HC){
			if(t.is_hi_preferred) {
				SLog.prn(2,", HI-preferred");
			} else {
				SLog.prn(2,", HC ");
				
			}
		}else{
			SLog.prn(2,", LC ");
		}
		
	}
	public static void prnPara(Task t)  {
		SLog.prnc(2, "tmp.add(new Task(");
		SLog.prnc(2, t.period);
		if (t.is_HC){
			SLog.prnc(2, ", "+t.c_l);
			SLog.prnc(2, ", "+t.c_h);
		} else {
			SLog.prnc(2, ", "+t.c_l);
		}
		SLog.prn(2, "));");

	}
	public static void prn(TaskMng tm) {
		for(Task t:tm.getTasks()) {
			prnTxt(t);
		}
		
	}
	public static void prnDetail(TaskMng tm) {
		double x=tm.getX();
		SLog.prn("tm x: "+x);
		for(Task t:tm.getTasks()) {
			prnOffline(t);
		}
		
	}
	public static void prnUtil(TaskMng tm) {
		SysInfo info=tm.getInfo();
		info.prnUtil();
	}

}
