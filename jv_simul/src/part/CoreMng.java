package part;



import java.util.Vector;

import util.Log;
import basic.Task;
import basic.TaskMng;
import basic.TaskSet;
import basic.TaskSetFix;


public class CoreMng {
	Vector<TaskMng> g_tm;
	public CoreMng() {
		g_tm=new Vector<TaskMng>();
	}
	public void addTS(TaskSet ts) {
		TaskSetFix tsf=new TaskSetFix(ts);
		g_tm.addElement(tsf.getTM());
	}
	public void prn() {
		int i=1;
		for(TaskMng tm:g_tm){
			Log.prn(2, "core "+i);
			i++;
			tm.prnShort();
		}
		
	}
	public void move() {
		Log.prn(2, "move ");
		Vector<TaskSet> v=getVec();
		TaskSet ts1=v.elementAt(0);
		TaskSet ts2=v.elementAt(1);
		Task t=ts1.removeLast();
		ts2.add(t);
		ts1.transform_Array();
		ts2.transform_Array();
		g_tm=new Vector<TaskMng>();
		addTS(ts1);
		addTS(ts2);
	}
	private Vector<TaskSet> getVec() {
		Vector<TaskSet> v=new Vector<TaskSet>();
		for(TaskMng tm:g_tm){
			v.add(tm.getTaskSet());
		}
		return v;
	}
	public TaskMng getTM(int i) {
		return g_tm.elementAt(i);
	}
	

}
