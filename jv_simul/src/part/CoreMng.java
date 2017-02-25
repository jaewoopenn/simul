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
	public void set_tm(int core,TaskMng tm) {
		g_tm[core]=tm;
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
		int i=0;
		for(TaskMng tm:g_tm){
			Log.prn(2, "core "+i);
			i++;
			tm.prn();
		}
		
	}
	public void move(Task tsk, int core) {
		tsk.is_moved=true;
		Log.prn(2, "task "+tsk.tid+" move cpu "+core);
		TaskSet ts=g_tm[core].getTaskSet();
		ts.add(tsk.getCopy());
		setTS_in(ts,core);
	}

	private void setTS_in(TaskSet ts, int core) {
		double x=g_tm[core].getInfo().getX();
		double lo_max=g_tm[core].getInfo().getLo_max();
		ts.transform_Array();
		setTS(core,ts);
		g_tm[core].setX(x);
		g_tm[core].setLo_max(lo_max);
		g_tm[core].set_cm(this);
		if(g_tsim[core]!=null)
			g_tsim[core].set_tm(g_tm[core]);
		
	}

	public void recover(int core) {
		TaskSet ts=g_tm[core].getTaskSet();
		ts.removeCore(core);
		setTS_in(ts,core);
	}

	

}
