package part;




import util.Log;
import basic.Task;
import basic.TaskMng;
import basic.TaskSet;
import basic.TaskSetFix;
import simul.TaskSimul;


public class CoreMng {
	private int g_size;
	private TaskMng[] g_tm;
	private TaskSimul[] g_tsim;
	public CoreMng(int size) {
		g_size=size;
		g_tm=new TaskMng[size];
		g_tsim=new TaskSimul[size];
	
	}
	
	public void setTS(int core, TaskSet ts) {
		TaskSetFix tsf=new TaskSetFix(ts);
		g_tm[core]=tsf.getTM();
	}
	public void setSim(int core, TaskSimul tsim) {
		g_tsim[core]=tsim;
	}
	public TaskMng getTM(int i) {
		return g_tm[i];
	}
	public TaskSet getTS(int i) {
		return g_tm[i].getTaskSet();
	}
	public TaskSimul getSim(int core) {
		return g_tsim[core];
	}
	public int size() {
		return g_size;
	}
	// action
	public void prn() {
		int i=1;
		for(TaskMng tm:g_tm){
			Log.prn(2, "core "+i);
			i++;
			tm.prnShort();
		}
		
	}
	public void move(Task tsk, int i) {
		Log.prn(2, "move ");
		TaskSet ts1=g_tm[0].getTaskSet();
		TaskSet ts2=g_tm[1].getTaskSet();
		Task t=ts1.removeLast();
		ts2.add(t);
		ts1.transform_Array();
		ts2.transform_Array();
	}
	

}
