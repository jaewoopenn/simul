package part;



import java.util.Vector;

import anal.Anal;
import anal.AnalEDF_VD;
import basic.Task;
import basic.TaskSet;
import util.Log;
import basic.TaskSetFix;

public class Partition {
	TaskSet g_ts;
	Vector<TaskSet> g_part;
	public Partition(TaskSet t) {
		g_ts=t;
		g_part=new Vector<TaskSet>();
	}
	public int anal() {
		g_ts.sortHi();
//		g_ts.prn();
		TaskSetFix tsf=new TaskSetFix();
		Anal anal=new AnalEDF_VD();

		for(Task t:g_ts.getArr()){
			tsf.add(t);
			anal.init(tsf.getTM());
			anal.prepare();
			double d=anal.getDtm();
			if (d>1){
				TaskSet ts=tsf.getTS();
				ts.removeLast();
				ts.transform_Array();
				g_part.addElement(ts);
				tsf=new TaskSetFix();
				tsf.add(t);
			}
		}
		TaskSet ts=tsf.getTS();
		ts.transform_Array();
		g_part.addElement(ts);
		return g_part.size();
	}
	public TaskSet getTS(int core){
		return g_part.elementAt(core);
	}
	public void prn(){
//		Log.prn(2,"aa"+g_part.size());
		for(TaskSet ts:g_part){
			ts.prn();
			Log.prn(2,"--");
		}
		
	}
	public int size() {
		return g_part.size();
	}

}
