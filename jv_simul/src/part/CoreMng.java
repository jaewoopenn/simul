package part;



import java.util.Vector;

import util.Log;
import basic.TaskMng;
import basic.TaskSet;
import basic.TaskSetFix;


public class CoreMng {
	Vector<TaskMng> g_tm;
	public CoreMng() {
		g_tm=new Vector<TaskMng>();
	}
	public void addTS(TaskSet ts) {
		TaskSetFix tsf=new TaskSetFix();
		tsf.setTaskSet(ts);
		g_tm.addElement(tsf.getTM());
	}
	public void prn() {
		for(TaskMng tm:g_tm){
			Log.prn(2, "core ");
			tm.prn();
		}
		
	}
	

}
