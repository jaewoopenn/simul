package Simul;

// 
import Util.Log;

public class Analysis {
	TaskMng tm;
	int[] plst;
	
	public void init(TaskMng mng) {
		tm=mng;
		tm.freezeTasks();
		plst=tm.getPeriods();
		
	}



}
