package Simul;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;

import Exp.Platform;
import Util.Log;

public class SimGen {
	private TaskGen tg;
	private ConfigGen g_cfg;
	public SimGen(ConfigGen cfg) {
		tg=new TaskGen();
		g_cfg=cfg;
	}
	public void gen() {
		TaskGen tg=new TaskGen();
		tg.setFlagMC(true);
		tg.setUtil(g_cfg.readDbl("u_lb"),g_cfg.readDbl("u_ub"));
		tg.setPeriod(g_cfg.readInt("p_lb"),g_cfg.readInt("p_ub"));
		tg.setTUtil(g_cfg.readDbl("tu_lb"),g_cfg.readDbl("tu_ub"));
		tg.setRatioLH(g_cfg.readDbl("r_lb"),g_cfg.readDbl("r_ub"));
		tg.setProbHI(g_cfg.readDbl("prob_hi"));
		int num=g_cfg.readInt("num");
		for(int i=0;i<num;i++){
			tg.generate();
			String subfix=g_cfg.readPar("subfix").trim();
			String mod=g_cfg.readPar("mod").trim();
			String fn=subfix+"/taskset_"+mod+"_"+i;
			tg.writeFile(fn);
		}
		
	}
	public int size(){
		return g_cfg.readInt("num");
	}
	public int load() {
		int num=g_cfg.readInt("num");
		int sum=0;
		
		for(int i=0;i<num;i++){
			sum+=load(i);
		}
		return sum;	
	}
	public int load(int i){
		TaskGen tg=new TaskGen();
		tg.setFlagMC(true);
		tg.setUtil(g_cfg.readDbl("u_lb"),g_cfg.readDbl("u_ub"));
		String subfix=g_cfg.readPar("subfix").trim();
		String mod=g_cfg.readPar("mod").trim();
		String fn=subfix+"/taskset_"+mod+"_"+i;
		tg.loadFile(fn);
		if(tg.check()==0){
			Log.prn(1, "err "+i);
			return 0;
		}
		TaskMng tm=new TaskMng();
		tm.setTasks(tg.getAll());
		tm.freezeTasks();
		double util=tm.getMCUtil();
		//System.out.format("task set %d MC util: %.3f\n" ,i,util);
		
		return Analysis.analEDF_VD(tm);
	}

}
