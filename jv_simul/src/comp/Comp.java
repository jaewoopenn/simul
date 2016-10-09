package comp;

import java.util.Vector;

import utilSim.Log;
import basic.Task;
import basic.TaskMng;


public class Comp {
	public int cid;
	private TaskMng g_tm;
	
	public void setTM(TaskMng tm){
		g_tm=tm;
	}

	public void prn() {
		Log.prn(1, "cid:"+cid);
		g_tm.prn();
	}

	public Task[] getTasks() {
		return g_tm.getTasks();
	}

	public int size() {
		return g_tm.size();
	}

}

