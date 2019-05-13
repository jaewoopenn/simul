package gen;

import basic.TaskMng;
import basic.TaskSetEx;
import util.FUtilSp;

public class SysLoad {
	private FUtilSp g_fu;
	public SysLoad(String fn) {
		g_fu=new FUtilSp(fn);
	}

	public void load() {
		g_fu.load();
		TaskSetEx.loadView(g_fu);
		
	}

	public String open() {
		g_fu.br_open();
		return g_fu.read();
		
	}
	public TaskMng loadOne() {
		boolean b=g_fu.readSplit("------");
		if(!b) 
			return null;
		TaskSetEx tsf=TaskSetEx.loadFile_in(g_fu);
		return tsf.getTM();
		
	}
	
}
