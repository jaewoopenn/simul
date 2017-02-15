package part;



import java.util.Vector;

import anal.AnalEDF;
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
	public void anal() {
		g_ts.sortHi();
		g_ts.prn();
		TaskSetFix tsf=new TaskSetFix();
		AnalEDF a=new AnalEDF();

		for(Task t:g_ts.getArr()){
			tsf.add(t);
//			tsf.stat();
			a.init(tsf.getTM());
			a.prepare();
			double d=a.getDtm();
			if (d>1){
				TaskSet ts=tsf.getTS();
				ts.removeLast();
				g_part.addElement(ts);
				tsf=new TaskSetFix();
				tsf.add(t);
			}
			Log.prn(2, d);
		}
		g_part.addElement(tsf.getTS());
		Log.prn(2,"aa"+g_part.size());
		for(TaskSet ts:g_part){
			ts.transform_Array();
			ts.prn();
			Log.prn(2,"--");
		}
	}

}
