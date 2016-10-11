package comp;

import java.util.Vector;

import basic.Task;
import basic.TaskFile;
import basic.TaskMng;
import basic.TaskMngPre;
import utilSim.FUtil;
import utilSim.RUtil;
import utilSim.Log;

public class CompFile {
	// file
	public static void writeFile(String fn,Vector<Comp> comps) {
		FUtil fu=new FUtil(fn);
		for(Comp c:comps){
			Task[] tasks=c.getTasks();
			fu.print("C,"+c.getID()+","+tasks.length+","+c.getAlpha());
			for(Task t:tasks)
				TaskFile.writeTask(fu,t);
		}
		fu.save();
		
	}




	public static CompMng loadFile(String f) {
	    FUtil fu=new FUtil(f);
	    fu.load();
	    CompMng cm=new CompMng();
	    for(int i=0;i<fu.size();i++){
	    	String line=fu.get(i);
	    	Comp c=TaskFile.loadComp(line, fu,i+1);
	    	if(c!=null){
	    		cm.addComp(c);
	    		i+=c.size();
	    	}
	    }
		return cm;
	}
	

}
