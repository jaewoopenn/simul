package gen;

import task.TaskMng;
import task.TaskSet;
import task.TaskSetUtil;
import util.MFile;

public class SysLoad {
	private MFile g_fu;
	public SysLoad(String fn) {
		g_fu=new MFile(fn);
	}

	public void load() {
		g_fu.load();
		TaskSetUtil.loadView(g_fu);
		
	}

	public String open() {
		g_fu.br_open();
		return g_fu.read();
		
	}
	public TaskMng loadOne() {
		boolean b=g_fu.readSplit("------");
		if(!b) 
			return null;
		TaskSet tsf=TaskSetUtil.loadFile_in(g_fu);
		
		return new TaskMng(tsf);
		
	}
	
}
