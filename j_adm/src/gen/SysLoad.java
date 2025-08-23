package gen;

import task.DTaskVec;
import task.TaskMng;
import task.TaskSet;
import task.TaskSetUtil;
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
	
	public DTaskVec loadOne2() {
		boolean b=g_fu.readUntil("------");
		if(!b) 
			return null;
		MList ml=MList.new_list();
		ml.copyFrom(g_fu);
		return TaskSetUtil.loadFile2(ml);
	}


	public void moveto(int n) {
		for (int i=0;i<n;i++) {
			loadOne2();
		}
	}
	
}
