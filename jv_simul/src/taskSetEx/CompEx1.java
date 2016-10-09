package taskSetEx;

import basic.Task;
import basic.TaskMng;
import basic.TaskMngPre;

import comp.Comp;
import comp.CompMng;


// Task Set MC
public class CompEx1 {
	public static CompMng getComp1(){
		CompMng cm=new CompMng();
		Comp c=new Comp();
		TaskMngPre tmp=new TaskMngPre();
		tmp.add(new Task(0,6,1));
		tmp.add(new Task(0,8,1));
		TaskMng tm=tmp.freezeTasks();
		c.setTM(tm);
		cm.addComp(c);
		
		c=new Comp();
		tmp=new TaskMngPre();
		tmp.add(new Task(0,7,1));
		tmp.add(new Task(0,9,1));
		tm=tmp.freezeTasks();
		c.setTM(tm);
		cm.addComp(c);
		return cm;
	}
	

}
