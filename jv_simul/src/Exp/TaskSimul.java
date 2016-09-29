package Exp;


import Basic.Task;
import Basic.TaskMng;
import Util.Log;

public class TaskSimul {
	TaskMng g_tm;
	JobSimul g_js;
	public TaskSimul(TaskMng m){
		g_tm=m;
	}
	public void init() {
		g_js=new JobSimul();
		g_js.addJob(0, 4, 2);
		g_js.addJob(1, 5, 1);
		g_js.simul(5);
	}
	
}
