package part;



import java.util.Vector;

import anal.Anal;
import basic.Task;
import basic.TaskMng;
import basic.TaskSet;
import part.CoreMng;
import util.Log;
import util.MUtil;
import basic.TaskSetFix;

public class Partition {
	TaskSet g_ts;
	Anal g_anal;
	Vector<TaskSet> g_part;
	public Partition(Anal a,TaskSet t) {
		g_ts=t;
		g_anal=a;
		g_part=new Vector<TaskSet>();
	}
	public void anal() {
		g_ts.sortHi();
//		g_ts.prn();
		TaskSetFix tsf=new TaskSetFix();

		for(Task t:g_ts.getArr()){
			tsf.add(t);
			g_anal.init(tsf.getTM());
			g_anal.prepare();
			double d=g_anal.getDtm();
//			Log.prn(2, "d:"+d+" x:"+g_anal.getX());
			if (d>1){
//				Log.prn(2, "d:"+d);
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
	public CoreMng getCoreMng() {
		CoreMng cm=new CoreMng(g_part.size());
		int i=0;
		for(TaskSet ts:g_part){
			cm.setTS(i,ts);
			i++;
		}
		return cm;
	}
	public boolean check() {
		for(int i:MUtil.loop(size())){
			TaskSet ts=g_part.elementAt(i);
			TaskSetFix tsf=new TaskSetFix(ts);
			TaskMng tm=tsf.getTM();
			if(!tm.check()){
				Log.prn(2, "cpu "+i+" error");
				return false;
			}
		}
		return true;
		
	}

}
