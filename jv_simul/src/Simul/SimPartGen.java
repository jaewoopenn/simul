package Simul;


import Basic.CompGen;
import Basic.CompGenParam;

public class SimPartGen {
	private CompGen tg;
	private double g_alpha=0;
	private int g_method=0;
	private int g_cpus=0;
	private ConfigCompGen g_cfg;
	public SimPartGen(ConfigCompGen cfg) {
		g_cfg=cfg;
		g_cpus=g_cfg.readInt("cpus");

	}
	public int prepare(){
		CompGenParam p=new CompGenParam();
		p.set_lt_lu(g_cfg.readDbl("lt_lu_lb"),g_cfg.readDbl("lt_lu_ub"));
		p.set_ht_lu(g_cfg.readDbl("ht_lu_lb"),g_cfg.readDbl("ht_lu_ub"));
		p.set_ratio(g_cfg.readDbl("r_lb"),g_cfg.readDbl("r_ub"));
		p.set_util(g_cfg.readDbl("u_lb"),g_cfg.readDbl("u_ub"));
		p.set_cpus(g_cpus);
		tg=new CompGen(p);
		
		return g_cfg.readInt("num");
	}
	public int genSet(int i)
	{
		tg.generate();
//		Log.prn(2, "util:"+tg.getMCUtil());
		String subfix=g_cfg.readPar("subfix").trim();
		String mod=g_cfg.readPar("mod").trim();
		String cpus=g_cfg.readPar("cpus").trim();
		String fn=subfix+"/taskset_"+mod+"_"+cpus+"_"+i;
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
	public int load() {
		int num=g_cfg.readInt("num");
		int sum=0;
		
		for(int i=0;i<num;i++){
			boolean b=load_one(i);
			if (b)
				sum++;
		}
		return sum;	
	}
	public boolean analPart(CompMng cm) {
		if(g_method==0) {
			cm.sortMC();
			PartAnal a=new PartAnal(cm,g_cpus);
			boolean b=a.partitionWF(g_alpha);
			return b;
		}
		else if(g_method==1) {
			cm.sortMC();
			PartAnal a=new PartAnal(cm,g_cpus);
			boolean b=a.partitionFF(g_alpha);
			return b;
		}
		else if(g_method==2) {
			cm.sortHI();
			PartAnal a=new PartAnal(cm,g_cpus);
			boolean b=a.partitionFF(g_alpha);
			return b;
		}
		else if(g_method==3) {
			cm.sortLO();
			PartAnal a=new PartAnal(cm,g_cpus);
			boolean b=a.partitionFF(g_alpha);
			return b;
		}
		else if(g_method==4) {
			cm.sortMC();
			PartAnal a=new PartAnal(cm,g_cpus);
			a.help1();
			boolean b=a.partitionWF(g_alpha);
			a.help2();
			return b;
		}
		else{
			cm.sortMC();
			PartAnal a=new PartAnal(cm,g_cpus);
			boolean b=a.partitionBF(g_alpha);
			return b;
		}
		
	}
	
	public boolean load_one(int i){
		CompGenParam p=new CompGenParam();
		CompGen tg=new CompGen(p);
		String subfix=g_cfg.readPar("subfix").trim();
		String mod=g_cfg.readPar("mod").trim();
		String cpus=g_cfg.readPar("cpus").trim();
		String fn=subfix+"/taskset_"+mod+"_"+cpus+"_"+i;
		tg.loadFile(fn);
		CompMng cm=tg.getCM();
		return analPart(cm);
	}

	// getting
	public int size(){
		return g_cfg.readInt("num");
	}
	public void set_alpha(double alpha) {
		g_alpha = alpha;
	}
	public void set_method(int m) {
		this.g_method = m;
	}
//	public void set_cpus(int cpus) {
//		this.g_cpus = cpus;
//	}

	
	
}
