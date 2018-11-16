package gen;

import basic.TaskMng;
import basic.TaskSetFile;
import util.FUtil;

public class SysLoad {
	private FUtil g_fu;
	public SysLoad(String fn) {
		g_fu=new FUtil(fn);
	}

	public void load() {
		g_fu.load();
		TaskSetFile.loadView(g_fu);
		
	}

	public void open() {
		g_fu.br_open();
		
	}
	public TaskMng loadOne() {
		boolean b=g_fu.readSplit("------");
		if(!b) 
			return null;
		TaskSetFile tsf=TaskSetFile.loadFile_in(g_fu);
		return tsf.getTM();
		
	}
	
}
