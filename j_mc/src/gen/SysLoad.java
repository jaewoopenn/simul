package gen;

import task.TSFile;
import task.TaskVec;
import util.MFile;
import util.MList;

public class SysLoad {
	private MFile g_fu;
	private String  g_rs=null;
	public SysLoad(String fn) {
		g_fu=new MFile(fn);
		g_fu.br_open();
		g_rs=g_fu.read();
	}

	
	public TaskVec loadOne() {
		boolean b=g_fu.readUntil("------");
		if(!b) 
			return null;
		MList ml=MList.new_list();
		ml.copyFrom(g_fu);
		return TSFile.loadFile(ml);
	}


	public void moveto(int n) {
		for (int i=0;i<n;i++) {
			loadOne();
		}
	}


	public int getNum() {
		return Integer.valueOf(g_rs).intValue();
	}
	
}
