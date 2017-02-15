package part;



import anal.AnalEDF;
import basic.Task;
import basic.TaskSet;
import util.Log;
import basic.TaskSetFix;

public class Partition {
	TaskSet g_ts;
	public Partition(TaskSet t) {
		g_ts=t;
	}
	public void anal() {
		g_ts.sortLo();
		g_ts.prn();
		TaskSetFix tsf=new TaskSetFix();
		AnalEDF a=new AnalEDF();

		for(Task t:g_ts.getArr()){
			tsf.add(t);
			tsf.stat();
			a.init(tsf.getTM());
			a.prepare();
			double d=a.getDtm();
			if (d>1){
				tsf=new TaskSetFix();
				tsf.add(t);
			}
			Log.prn(2, d);
		}
	}

}
