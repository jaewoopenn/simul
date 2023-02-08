package part;




import util.Log;
import util.MUtil;
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
		TaskSet ts2=g_tm[1].getTaskSet();
		ts2.add(tsk.getCopy());
		ts2.transform_Array();
		setTS(1,ts2);
		g_tm[1].set_cm(this);
		g_tsim[1].set_tm(g_tm[1]);
//		g_tsim[1].getTM().prn();
//		g_cm.getSim(1).getTM().prn();
//		System.exit(0);
	}

	public void recover(int core) {
		for(int i:MUtil.loop(g_size)){
			if(i==core)
				continue;
			TaskSet ts=g_tm[i].getTaskSet();
			ts.removeCore(core);
			ts.transform_Array();
			setTS(i,ts);
			g_tm[i].set_cm(this);
			
			g_tsim[i].set_tm(g_tm[i]);

		}
	}
	

}
