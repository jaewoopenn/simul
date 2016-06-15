package Simul;


import Basic.CompGen;
import Basic.CompGenParam;
import Basic.TaskGen;
import Basic.TaskMng;
import Util.Log;

public class SimPartGen {
	private CompGen tg;
	private ConfigCompGen g_cfg;
	public SimPartGen(ConfigCompGen cfg) {
		g_cfg=cfg;
	}
	public int prepare(){
		CompGenParam p=new CompGenParam();
		p.set_lt_lu(g_cfg.readDbl("lt_lu_lb"),g_cfg.readDbl("lt_lu_ub"));
		p.set_ht_lu(g_cfg.readDbl("ht_lu_lb"),g_cfg.readDbl("ht_lu_ub"));
		p.set_ratio(g_cfg.readDbl("r_lb"),g_cfg.readDbl("r_ub"));
		p.set_util(g_cfg.readDbl("u_lb"),g_cfg.readDbl("u_ub"));
		tg=new CompGen(p);
		
		return g_cfg.readInt("num");
	}
	public int genSet(int i)
	{
		tg.generate();
//		Log.prn(2, "util:"+tg.getMCUtil());
		String subfix=g_cfg.readPar("subfix").trim();
		String mod=g_cfg.readPar("mod").trim();
		String fn=subfix+"/taskset_"+mod+"_"+i;
//		tg.prn2(2);
		tg.writeFile(fn);
		return 1;
	}
	public void gen() {
		int num=prepare();
		for(int i=0;i<num;i++){
//			Log.prn(2, i+"");
			genSet(i);
		}
		
	}
	public int load(double alpha) {
		int num=g_cfg.readInt("num");
		int sum=0;
		
		for(int i=0;i<num;i++){
			CompMng cm=load_one(i);
//			Log.prn(2, tm.size());
			sum+=analComp(cm,alpha);
		}
		return sum;	
	}
	public int analComp(CompMng cm,double alpha) {
//		CompMng cm=new CompMng();
//		cm.load(tm);
//		CompAnal a=new CompAnal(cm);
//		a.compute_X();
//		a.set_alpha(alpha);
//		TaskMng ifc=a.getInterfaces();
////		tm.prn();
//		return Analysis.anal_EDF_VD(ifc);
		return -1;
	}
	
	public CompMng load_one(int i){
		CompGenParam p=new CompGenParam();
		CompGen tg=new CompGen(p);
		String subfix=g_cfg.readPar("subfix").trim();
		String mod=g_cfg.readPar("mod").trim();
		String fn=subfix+"/taskset_"+mod+"_"+i;
		tg.loadFile(fn);
		if(tg.check()==0){
			Log.prn(2, "err "+i);
			tg.prn(2);
			return null;
		}
		CompMng cm=tg.getCM();
		return cm;
	}

	// getting
	public int size(){
		return g_cfg.readInt("num");
	}

	
	
}
