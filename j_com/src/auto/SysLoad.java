package auto;

import task.TaskSet;
import task.TaskVec;
import task.TaskUtil;
import util.MFile;
import util.MList;

public class SysLoad {
	private MFile g_fu;
	public SysLoad(String fn) {
		g_fu=new MFile(fn);
	}


	public String open() {
		g_fu.br_open();
		return g_fu.read();
		
	}
	public TaskSet loadOne() {
		boolean b=g_fu.readSplit("------");
		if(!b) 
			return null;
		MList ml=new MList();
		ml.copy(g_fu);
//		ml.prn();
		TaskVec tsf=TaskUtil.loadML(ml);
		return new TaskSet(tsf);
		
	}
	public TaskSet loadOne(int n) {
		for(int i=0;i<n;i++) {
			boolean b=g_fu.readSplit("------");
			if(!b) 
				return null;
		}
		MList ml=new MList();
		ml.copy(g_fu);
		TaskVec tsf=TaskUtil.loadML(ml);
		return new TaskSet(tsf);
		
	}	
}
