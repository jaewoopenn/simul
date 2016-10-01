package Exp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;

import Basic.TaskGen;
import Basic.TaskGenMC;
import Basic.TaskMng;
import Simul.ConfigGen;
import Util.Log;

public class ExpSimul {
	private ConfigGen g_cfg;
	public boolean g_prn=true;
	
	public ExpSimul(ConfigGen cfg) {
		g_cfg=cfg;
	}
	public int size(){
		return g_cfg.readInt("num");
	}
	public int load(int dur) {
		int num=g_cfg.readInt("num");
		int sum=0;
		
		for(int i=0;i<num;i++){
			TaskGen tg=new TaskGen();
			String fn=g_cfg.get_fn(i);
			TaskMng tm=tg.loadFileTM(fn);
			double util=tg.getUtil();
			TaskSimul ts=new TaskSimul(tm);
			int ret=ts.exec(dur);
			sum+=ret;
			if(g_prn)
				System.out.format("task %d util: %.3f ret: %d\n" ,i,util,ret);
			if(util>1 && ret==1)
				Log.prn(2,"util>1 but sch");
			if(util<=1 && ret==0)
				Log.prn(2,"util<=1 but not sch");
//			Log.prn(2, "task "+i+" util:"+util+" ret:"+ret);
		}
		return sum;
		
	}

}
