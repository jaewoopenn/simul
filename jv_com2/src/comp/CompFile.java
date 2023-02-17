package comp;

import java.util.Vector;

import task.Task;
import task.TaskSeq;
import task.TaskSetUtil;
import util.MList;
import util.SLog;

public class CompFile {
	// file
	public static void writeFile(String fn,Vector<Comp> comps) {
		MList fu=new MList();
		for(Comp c:comps){
			Task[] tasks=c.getTaskSet();
			fu.add("C,"+c.getID()+","+tasks.length+","+c.getAlpha());
			for(Task t:tasks)
				TaskSetUtil.writeTask(fu,t);
		}
		fu.save(fn);
		
	}




	public static CompMng loadFile(String f) {
		TaskSeq.reset();
	    MList fu=new MList(f);
//	    fu.prn();
	    CompMng cm=new CompMng();
	    for(int i=0;i<fu.size();i++){
	    	String line=fu.get(i);
	    	SLog.prn(1, line);
	    	Comp c=TaskSetUtil.loadComp(line, fu,i+1);
	    	if(c!=null){
	    		cm.addComp(c);
	    		i+=c.size();
	    	}
	    }
//	    cm.prn();
		return cm;
	}
	

}
