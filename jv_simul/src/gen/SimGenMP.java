package gen;

import anal.Anal;
import basic.TaskMng;
import basic.TaskSetFix;
import part.Partition;

public class SimGenMP extends SimGen {
	private int g_ncpu;
	public SimGenMP(ConfigGen cfg,int n) {
		this(cfg,null,n);
	}

	public SimGenMP(ConfigGen cfg,Anal an,int n) {
		super(cfg,an);
		g_ncpu=n;
	}

	protected int check() {
		
		TaskSetFix tsf=new TaskSetFix(g_tg.getAll());
		TaskMng tm=tsf.getTM();
		if(tm.getTasks().length==0) return 0;
		if(!g_isCheck)
			return 1;
		Partition p=new Partition(g_anal,tm.getTaskSet());
		p.anal();
		if(p.size()>g_ncpu)
			return 0;
		return 1;
		
	}
}
