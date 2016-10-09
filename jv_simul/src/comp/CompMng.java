package comp;

import java.util.Vector;

import basic.Task;
import basic.TaskFile;
import basic.TaskMng;
import utilSim.FUtil;
import utilSim.Log;

public class CompMng {
	private Vector<Comp> g_comp;
	public CompMng() {
		g_comp=new Vector<Comp>();
	}

	
	
	
	public void addComp(Comp c) {
		int cid=g_comp.size();
		c.cid=cid;
		g_comp.add(c);		
	}
	public Comp getComp(int i) {
		return g_comp.elementAt(i);
	}


	public int getSize() {
		return g_comp.size();
	}


	public void prn(){
		Log.prn(2, "tot:"+g_comp.size());
		for(Comp c:g_comp){
			c.prn();
		}
	}




	public void writeFile(String fn) {
		FUtil fu=new FUtil(fn);
		for(Comp c:g_comp){
			Task[] tasks=c.getTasks();
			fu.print("C,"+c.cid+","+tasks.length);
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
