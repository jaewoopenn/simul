package Simul;


import Util.Log;

public class SimCompGen {
	private TaskGen tg;
	private ConfigGen g_cfg;
	private int g_max_com=0;
	public SimCompGen(ConfigGen cfg) {
		tg=new TaskGen();
		g_cfg=cfg;
	}
	public void setMaxCom(int c){
		g_max_com=c;
	}
	public int prepare(){
		tg=new TaskGen();
		tg.setUtil(g_cfg.readDbl("u_lb"),g_cfg.readDbl("u_ub"));
		tg.setPeriod(g_cfg.readInt("p_lb"),g_cfg.readInt("p_ub"));
		tg.setTUtil(g_cfg.readDbl("tu_lb"),g_cfg.readDbl("tu_ub"));
		tg.setRatioLH(g_cfg.readDbl("r_lb"),g_cfg.readDbl("r_ub"));
		tg.setProbHI(g_cfg.readDbl("prob_hi"));
		return g_cfg.readInt("num");
	}
	public int genSet(int i)
	{
		tg.generate();
//		Log.prn(2, "util:"+tg.getMCUtil());
		String subfix=g_cfg.readPar("subfix").trim();
		String mod=g_cfg.readPar("mod").trim();
		String fn=subfix+"/taskset_"+mod+"_"+i;
		tg.assignComp(g_max_com);
//		tg.prn2(2);
		tg.writeFile2(fn);
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
			TaskMng tm=load_one(i);
//			Log.prn(2, tm.size());
			sum+=analComp(tm,alpha);
		}
		return sum;	
	}
	public int analComp(TaskMng tm,double alpha) {
		CompMng cm=new CompMng();
		cm.load(tm);
		CompAnal a=new CompAnal(cm);
		a.compute_X();
//		a.set_alpha(0.0);
		a.set_alpha(alpha);
//		a.set_alpha(1.0);
		TaskMng ifc=a.getInterfaces();
//		tm.prn();
		return Analysis.anal_EDF_VD(ifc); 
	}
	
	public TaskMng load_one(int i){
		TaskGen tg=new TaskGen();
		tg.setUtil(g_cfg.readDbl("u_lb"),g_cfg.readDbl("u_ub"));
		String subfix=g_cfg.readPar("subfix").trim();
		String mod=g_cfg.readPar("mod").trim();
		String fn=subfix+"/taskset_"+mod+"_"+i;
		tg.loadFile2(fn);
		if(tg.check()==0){
			Log.prn(2, "err "+i);
			tg.prn(2);
			return null;
		}
		TaskMng tm=new TaskMng();
		tm.setTasks(tg.getAll());
		tm.freezeTasks();
		tm.sort();
		return tm;
	}

	// getting
	public int size(){
		return g_cfg.readInt("num");
	}

	
	
}
