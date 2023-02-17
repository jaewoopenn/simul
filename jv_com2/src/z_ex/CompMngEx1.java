package z_ex;

import comp.Comp;
import comp.CompMng;
import task.Task;
import task.TaskVec;

// Task Set MC
public class CompMngEx1 {
		
	public static CompMng getCompMng1(){
		CompMng cm=new CompMng();
		Comp c=new Comp(0);
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(6,1));
		tmp.add(new Task(8,1));
		c.setTM(tmp.getTM());
		cm.addComp(c);
		
		c=new Comp(0);
		tmp=new TaskVec();
		tmp.add(new Task(7,1));
		tmp.add(new Task(9,1));
		c.setTM(tmp.getTM());
		cm.addComp(c);
		return cm;
	}
	
	public static CompMng getCompMng2(){
		CompMng cm=new CompMng();
		Comp c=new Comp(0.2);
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(8,1));
		tmp.add(new Task(12,1,5));
		c.setTM(tmp.getTM());
		cm.addComp(c);
//		
		c=new Comp(0.3);
		tmp=new TaskVec();
		tmp.add(new Task(8,3));
		tmp.add(new Task(12,1,5));
		c.setTM(tmp.getTM());
		cm.addComp(c);
		return cm;
	}

	public static CompMng getCompMng3(){
		CompMng cm=new CompMng();
		Comp c=new Comp(0.2);
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(16,1));
		tmp.add(new Task(27,1));
		tmp.add(new Task(12,1,5));
		c.setTM(tmp.getTM());
		cm.addComp(c);
		
		c=new Comp(0.3);
		tmp=new TaskVec();
		tmp.add(new Task(16,1));
		tmp.add(new Task(30,1));
		tmp.add(new Task(120,3));
		tmp.add(new Task(12,1,5));
		c.setTM(tmp.getTM());
		cm.addComp(c);
		return cm;
	}
	

}
