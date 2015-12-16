package Exp;

import java.util.Vector;

import Simul.Task;
import Simul.TaskMng;
import Util.Log;

public class Platform {
	TaskMng tm;
	JobMng jm;
	int cur_t;
	int[] plst;
	int g_mode=0;
	int g_ms;
	public Platform(){
		tm=new TaskMng();
		
	}
	public void addTask(int p, int e) {
		tm.addTask(p, e);
	}
	public void addHiTask(int p, int l, int h) {
		tm.addHiTask(p, l,h);
		
	}
	
	public int post(int dur){
		tm.freezeTasks();
		plst=tm.getPeriods();
		
		jm=new JobMng();
		cur_t=0;
		return simul(dur);
	}
	public void init(TaskMng mng) {
		tm=mng;
		tm.freezeTasks();
		plst=tm.getPeriods();
		
		jm=new JobMng();
		cur_t=0;
	}
	public int simul(int et){
		boolean bSuc;
		while(cur_t<et){
			bSuc=jm.dlCheck(cur_t);
			if(!bSuc) return 0;
			if(g_mode==0&&cur_t>=g_ms){
				Log.prn(1,"mode-switch");
				g_mode=1;
				jm.modeswitch();
			}
			Log.prnc(1,"t:"+cur_t+" rel:");
			relCheck();
			bSuc=jm.progress(cur_t);
			if(!bSuc) return 0;
			cur_t++;
		}
		Log.prn(1, "*** Left Jobs at time "+cur_t+" ***");
		jm.prn();
		return jm.endCheck(et);
	}

	
	private void relCheck(){
		
		for(int i=0;i<tm.size();i++){
			if (cur_t%plst[i]!=0){
				Log.prnc(1,"-");
				continue;
			}
//			Log.prn(2,"rel "+i);
			Task tsk=tm.getTask(i);
//			Log.prn(2, "p:"+tsk.period+" e:"+tsk.exec);
			if(g_mode==0){
				if(tsk.is_HI){
					jm.insertJob(tsk.tid,cur_t+(int)tsk.vd,tsk.c_l, tsk.c_h-tsk.c_l);
				} else {
					jm.insertJob(tsk.tid,cur_t+(int)tsk.vd,tsk.c_l);
				}
				Log.prnc(1,"+");
			} else {
				if(tsk.is_HI){
					jm.insertJob(tsk.tid,cur_t+tsk.period,tsk.c_h);
					Log.prnc(1,"+");
				} else {
					Log.prnc(1,"-");
				}
					
			}
		}
		Log.prnc(1, " exe:");
	}
	public void setMS(int t) {
		g_ms=t;
	}


}
