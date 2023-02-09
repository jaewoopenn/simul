package comp;

import java.util.Vector;

import task.Task;
import task.TaskSetUtil;
import util.MList;

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
	    MList fu=new MList(f);
	    CompMng cm=new CompMng();
	    for(int i=0;i<fu.size();i++){
	    	String line=fu.get(i);
	    	Comp c=TaskSetUtil.loadComp(line, fu,i+1);
	    	if(c!=null){
	    		cm.addComp(c);
	    		i+=c.size();
	    	}
	    }
		return cm;
	}
	

}
