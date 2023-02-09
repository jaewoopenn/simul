package gen;

import util.MUtil;
import anal.Anal;
import task.TaskMng;
import task.TaskSet;
import task.TaskSetMC;

public class SimGenTM extends SimGen {
	public SimGenTM(ConfigGen cfg) {
		this(cfg,null);
	}

	public SimGenTM(ConfigGen cfg,Anal an) {
		super(cfg,an);
	}

	protected int check() {
		TaskSetMC tsf=new TaskSetMC(new TaskSet(g_tg.getAll()));
		TaskMng tm=tsf.getTM();
		if(tm.getTasks().length==0) return 0;
		if(!g_isCheck)
			return 1;
		g_anal.init(tm);
		g_anal.prepare();
		
		return MUtil.btoi(g_anal.is_sch());
	}
	
}
