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
	public int genSet(int i,boolean b)
	{
		tg.generate();
		if(b){
			TaskMng tm=new TaskMng();
			tm.setTasks(tg.getAll());
			tm.freezeTasks();
			int rs=Analysis.analEDF_VD(tm);
			if(rs==0)
				return 0;
		}
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
			genSet(i,false);
		}
		
	}
	public void gen2() {
		int num=prepare();
		int i=0;
		while(i<num){
//			Log.prn(2, i+"");
			int rs=genSet(i,true);
			if(rs==1)
				i++;
		}
		
	}
	
	public TaskMng genOne(){
		tg.generate();
		TaskMng tm=new TaskMng();
		tm.setTasks(tg.getAll());
		tm.freezeTasks();
		return tm;	
	}
	public int size(){
		return g_cfg.readInt("num");
	}
	public int load(int anal) {
		int num=g_cfg.readInt("num");
		int sum=0;
		
		for(int i=0;i<num;i++){
			TaskMng tm=load_one(i);
			sum+=process(tm,anal);
		}
		return sum;	
	}
	public int load2(int anal) {
		int num=g_cfg.readInt("num");
		int v=0;
		
		for(int i=0;i<num;i++){
			TaskMng tm=load_one(i);
			v=process(tm,anal);
			Log.prn(2, "v:"+v);
		}
		return 0;	
	}
	
	public double load3(int anal,double p) {
		int num=g_cfg.readInt("num");
		double v=0;
//		num=100;
		for(int i=0;i<num;i++){
			TaskMng tm=load_one(i);
//			int rs=Analysis.analEDF_VD(tm);
//			if(rs==0){
//				Log.err("err");
//			}
//			tm.prn();
			v+=process2(tm,anal,p);
			Log.prn(1, "v:"+v);
		}
		return v/num;	
	}

	
	
	public TaskMng load_one(int i){
		TaskGen tg=new TaskGen();
		tg.setFlagMC(true);
		tg.setUtil(g_cfg.readDbl("u_lb"),g_cfg.readDbl("u_ub"));
		String subfix=g_cfg.readPar("subfix").trim();
		String mod=g_cfg.readPar("mod").trim();
		String fn=subfix+"/taskset_"+mod+"_"+i;
		tg.loadFile(fn);
		if(tg.check()==0){
			Log.prn(1, "err "+i);
			return null;
		}
		TaskMng tm=new TaskMng();
		tm.setTasks(tg.getAll());
		tm.freezeTasks();
		tm.sort();
		return tm;
	}
	
	public int process(TaskMng tm, int anal) {
		double util=tm.getMCUtil();
//		System.out.format("task set %d MC util: %.3f\n" ,i,util);
		double v;
		switch(anal)
		{
		case 0:
			return Analysis.analEDF(tm);
		case 1:
			return Analysis.analEDF_VD(tm);
		case 2:
			return Analysis.analEDF_TM(tm);
		case 3:
			return Analysis.analEDF_TM_S(tm);

//		case 3:
//			return Analysis.getRespEDF(tm);
//		case 4:
//			return Analysis.getRespEDF_VD(tm);
//		case 5:
//			return Analysis.getRespEDF_TM(tm);
		default:
			Log.prn(2,"anal ID check");
		}
		return -1;
	}
	public double process2(TaskMng tm, int anal,double p) {
		switch(anal)
		{
		case 6:
			return Analysis.getDrop_EDF_VD(tm,p);
		case 7:
			return Analysis.getDrop_EDF_TM_S(tm,p);
		default:
			Log.prn(2,"anal ID check");
		}
		return -1;
	}

	
}
