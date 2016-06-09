package Simul;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;

import Exp.Platform;
import Util.Log;

public class SimCompGen {
	private TaskGen tg;
	private ConfigGen g_cfg;
	public SimCompGen(ConfigGen cfg) {
		tg=new TaskGen();
		g_cfg=cfg;
	}
	public int prepare(){
		tg=new TaskGen();
		tg.setFlagMC(true);
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
	
	public int size(){
		return g_cfg.readInt("num");
	}

	
	
}
