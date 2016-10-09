package taskSetEx;

import basic.Task;
import basic.TaskMng;
import basic.TaskMngPre;
import comp.Comp;


// Task Set MC
public class CompEx1 {
	public static Comp getComp1(){
		Comp c=new Comp(0);
		TaskMngPre tmp=new TaskMngPre();
		tmp.add(new Task(0,6,1));
		tmp.add(new Task(0,8,1));
		TaskMng tm=tmp.freezeTasks();
		c.setTM(tm);
		return c;
	}
	public static Comp getComp2(){
		Comp c=new Comp(0.2);
		TaskMngPre tmp=new TaskMngPre();
		tmp.add(new Task(0,8,1));
		tmp.add(new Task(0,12,1,5));
		TaskMng tm=tmp.freezeTasks();
		c.setTM(tm);
		return c;
	}
		
	public static Comp getComp3(){
		Comp c=new Comp(0.5);
		TaskMngPre tmp=new TaskMngPre();
		tmp.add(new Task(0,18,1));
		tmp.add(new Task(0,28,1));
		tmp.add(new Task(0,38,2));
		tmp.add(new Task(0,48,3));
		tmp.add(new Task(0,6,1,5));
		TaskMng tm=tmp.freezeTasks();
		c.setTM(tm);
		return c;
	}
	

}
