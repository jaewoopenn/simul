package gen;

import basic.TaskMng;
import basic.TaskSetFile;
import util.FUtil;
import util.Log;

public class SysLoad {
	private ConfigGen g_cfg;
	private FUtil g_fu;
	public SysLoad(String cfg) {
		g_cfg=new ConfigGen(cfg);
		g_cfg.readFile();
	}

	public void load() {
		String fn=g_cfg.get_fn();
		FUtil fu=new FUtil(fn);
		
		fu.load();
		TaskSetFile.loadView(fu);
		
	}

	public void open() {
		String fn=g_cfg.get_fn();
		g_fu=new FUtil(fn);
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
