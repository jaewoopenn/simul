package comp;

import gen.ConfigGen;
import gen.TaskGenParam;
import util.MOut;
import util.MUtil;
import anal.AnalEDF_AD_E;
import anal.AnalEDF_VD;

public class SimCompGen {
	private CompGen g_cg;
	private ConfigGen g_cfg;
	private boolean g_isCheck=false;
	public SimCompGen(ConfigGen cfg) {
		g_cfg=cfg;
	}
	public int prepare(){
		CompGenParam cgp=new CompGenParam();
		cgp.setUtil(g_cfg.readDbl("u_lb"),g_cfg.readDbl("u_ub"));
		cgp.setCUtil(g_cfg.readDbl("c_lb"),g_cfg.readDbl("c_ub"));
		cgp.setAlpha(g_cfg.readDbl("a_lb"),g_cfg.readDbl("a_ub"));
		
		TaskGenParam tgp=new TaskGenParam();
		tgp.setPeriod(g_cfg.readInt("p_lb"),g_cfg.readInt("p_ub"));
		tgp.setTUtil(g_cfg.readDbl("tu_lb"),g_cfg.readDbl("tu_ub"));
		tgp.setRatioLH(g_cfg.readDbl("r_lb"),g_cfg.readDbl("r_ub"));
		tgp.setProbHI(g_cfg.readDbl("prob_hi"));
		g_cg=new CompGen(cgp,tgp);
		return g_cfg.readInt("num");
	}
	public void gen(boolean isCheck) {
		g_isCheck=isCheck;
		int num=prepare();
		int i=0;
		String fn=g_cfg.get_dir();
		MOut.makeDir(fn);
		while(i<num){
//			Log.prn(2, i+"");
			CompMng cm=g_cg.generate();
			int rs=check(cm);
			if(rs==0)
				continue;
			writeSys(i,cm);
			i++;
		}
	}
	
	public int writeSys(int i, CompMng cm)
	{
		String fn=g_cfg.get_fn(i);
		CompFile.writeFile(fn,cm.getComps());
		return 1;
	}

	private int check(CompMng cm) {
		if(g_cg.check(cm)==0)
			return 0;
		if(!g_isCheck)
			return 1;
		
//		AnalEDF_AD_E a=new AnalEDF_AD_E();
//		a.init(cm.getTM());
//		a.prepare();
//		return MUtil.btoi(a.is_sch());
		cm.part();

		AnalComp ac=new AnalComp(cm);
		ac.computeX();
		cm.prn();
		int det=ac.anal(1);
		return det;
		
	}
	
}
