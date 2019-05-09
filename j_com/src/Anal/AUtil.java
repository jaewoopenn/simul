package Anal;

import com.PRM;

import basic.Task;
import basic.TaskMng;
import util.S_Log;

public class AUtil {
	public static double computeRBF(Task[] ts,int i, int t) {
		double r=0;
		for(int j=0;j<i;j++) {
			if(j==i-1) {
				r+=ts[j].exec;
			} else {
				r+=Math.floor(t*1.0/ts[j].period)*ts[j].exec;
				
			}
		}
		
		return r;
	}

	public static boolean checkSch_ind(PRM p, TaskMng tm, int i, int end_t) {
		int log_lv=0;
//		int log_lv=1;
		S_Log.prn(log_lv, "t \t sup \t req ");
		for(int t=0;t<end_t;t++) {
			double s=p.sbf(t);
			double r=AUtil.computeRBF(tm.getArr(),i,t);
			String st=t+"\t"+s+"\t"+r+"\t";
			if (s>r) {
				st+=">>>>>";
				S_Log.prn(log_lv,st );
				return true;
			} else {
				st+="<";
			}
			S_Log.prn(log_lv,st );
		}
		
		return false;
	}
}
