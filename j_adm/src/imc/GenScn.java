package imc;

import task.Task;
import task.TaskMng;
import util.SLog;

public class GenScn {
	TaskMng tm;
	public GenScn(TaskMng tm) {
		this.tm=tm;
	}
	public void loop(int dur) {
		tm.prn();
		boolean b=false;
		for(int t=0;t<dur;t++) {
			b=false;
			String s="";
			s+="t: "+t+" ";
			for(Task tsk:tm.getTasks()){
				if (t%tsk.period==0){
					s+=tsk.tid+" ";
					b=true;
				}
			}
			if(b)
				SLog.prn(1, s);

		}
		
	}
}
