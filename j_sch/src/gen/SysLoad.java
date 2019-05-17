package gen;

import basic.TaskMng;
import basic.TaskSetMC;
import basic.TaskSetUtil;
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
	public TaskMng loadOne() {
		boolean b=g_fu.readSplit("------");
		if(!b) 
			return null;
		MList ml=new MList();
		ml.copy(g_fu);
		TaskSetMC tsf=TaskSetUtil.loadFile_in(ml);
		return tsf.getTM();
		
	}
	
}
