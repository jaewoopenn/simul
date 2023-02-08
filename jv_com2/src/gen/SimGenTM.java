package gen;

import basic.TaskMng;
import basic.TaskSetFix;
import util.MUtil;
import anal.Anal;

public class SimGenTM extends SimGen {
	public SimGenTM(ConfigGen cfg) {
		this(cfg,null);
	}

	public SimGenTM(ConfigGen cfg,Anal an) {
		super(cfg,an);
	}

	protected int check() {
		TaskSetFix tsf=new TaskSetFix(g_tg.getAll());
		TaskMng tm=tsf.getTM();
		if(tm.getTasks().length==0) return 0;
		if(!g_isCheck)
			return 1;
		g_anal.init(tm);
		g_anal.prepare();
		
		return MUtil.btoi(g_anal.isScheduable());
	}
	
}
