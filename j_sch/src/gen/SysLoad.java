package gen;

import basic.TaskSetFile;
import util.FUtil;
import util.Log;

public class SysLoad {
	private ConfigGen g_cfg;
	
	public SysLoad(String cfg) {
		g_cfg=new ConfigGen(cfg);
		g_cfg.readFile();
	}

	public void load() {
		String fn=g_cfg.get_fn();
		Log.prn(1, fn);
		FUtil fu=new FUtil(fn);
		
		fu.load();
		TaskSetFile.loadView(fu);
		
	}
	
}
