package Anal;

import com.PRM;

import basic.Task;
import basic.TaskMng;
import util.S_Log;

public class Util_RM {
	public static double computeRBF(Task[] ts,int i, int t) {
		double r=0;
		for(int j=0;j<=i;j++) {
			if(j==i) {
				r+=ts[j].exec;
			} else {
				r+=Math.floor(t*1.0/ts[j].period)*ts[j].exec;
				
			}
		}
		
		return r;
	}

	public static boolean checkSch_ind(PRM p, TaskMng tm, int i, int end_t) {
		int log_lv=1;
		S_Log.prn(log_lv, "t \t sup \t req ");
		for(int t=1;t<=end_t;t++) {
			double s=p.sbf(t);
			double r=Util_RM.computeRBF(tm.getArr(),i,t);
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

	public static boolean checkSch(PRM p, TaskMng tm) {
		for(int i=0;i<tm.size();i++) {
			int end_t=tm.getTask(i).period;
			if(!checkSch_ind(p,tm,i,end_t))
				return false; // 하나라도 안되면 실패
				
		}
		return true; // 모두 성공하면 OK
	}

	public static double getExec(TaskMng tm, int p) {
		double exec=0;
		for(int i=0;i<tm.size();i++) {
			int end_t=tm.getTask(i).period;
			for(int t=1;t<=end_t;t++) {
				exec=Math.max(exec,getExec(tm,p,i,t));
			}
		}		
		return exec;
	}

	private static double getExec(TaskMng tm, int p, int i, int t) {
		return 1;
	}
}
