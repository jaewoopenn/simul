package part;



import basic.TaskSet;
//import utill.Log;

public class Partition {
	TaskSet g_ts;
	public Partition(TaskSet t) {
		g_ts=t;
	}
	public void anal() {
		g_ts.sortLo();
		
		g_ts.prn();
	}

}
