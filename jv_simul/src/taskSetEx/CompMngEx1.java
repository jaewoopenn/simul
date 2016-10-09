package taskSetEx;

import basic.Task;
import basic.TaskMng;
import basic.TaskMngPre;

import comp.Comp;
import comp.CompMng;


// Task Set MC
public class CompMngEx1 {
		
	public static CompMng getCompMng1(){
		CompMng cm=new CompMng();
		Comp c=new Comp(0);
		TaskMngPre tmp=new TaskMngPre();
		tmp.add(new Task(0,6,1));
		tmp.add(new Task(0,8,1));
		TaskMng tm=tmp.freezeTasks();
		c.setTM(tm);
		cm.addComp(c);
		
		c=new Comp(0);
		tmp=new TaskMngPre();
		tmp.add(new Task(0,7,1));
		tmp.add(new Task(0,9,1));
		tm=tmp.freezeTasks();
		c.setTM(tm);
		cm.addComp(c);
		return cm;
	}
	public static CompMng getCompMng2(){
		CompMng cm=new CompMng();
		Comp c=new Comp(0.2);
		TaskMngPre tmp=new TaskMngPre();
		tmp.add(new Task(0,8,1));
		tmp.add(new Task(0,12,1,5));
		TaskMng tm=tmp.freezeTasks();
		c.setTM(tm);
		cm.addComp(c);
		
		c=new Comp(0.3);
		tmp=new TaskMngPre();
		tmp.add(new Task(0,8,3));
		tmp.add(new Task(0,12,1,5));
		tm=tmp.freezeTasks();
		c.setTM(tm);
		cm.addComp(c);
		return cm;
	}
	

}
