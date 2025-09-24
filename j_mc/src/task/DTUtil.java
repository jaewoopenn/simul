package task;

import java.util.Vector;

import util.MLoop;
import util.SLog;

public class DTUtil {
	public static void prn(DTaskVec t) {
		int n=t.getStageNum();
		for(int i:MLoop.on(n)) {
			SLog.prn("stage: "+i);
			SLog.prn("class: "+t.getClass(i));
			TaskMng tm=getTM(t,i);
			TaskUtil.prn(tm);
			
		}
	}

	public static void copy(DTaskVec tasks, int src, int dst) {
		Vector<Task> srcV=tasks.getVec(src);
		TaskVec tv=new TaskVec();
		for(Task t:srcV) {
			Task tsk=t.copy();
			tv.add(tsk);
		}
		tasks.setVec(dst, tv);
	}
	private static TaskMng getTM(DTaskVec dt, int stage){
		TaskSet ts=new TaskSet(dt.getVec(stage));
		return ts.getTM();
	}
	
	public static TaskMng getCurTM(DTaskVec dt){
		return getTM(dt,dt.getCurSt());
	}
	
}
