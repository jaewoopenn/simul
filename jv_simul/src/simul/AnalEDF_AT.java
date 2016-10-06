package simul;

import basic.Task;
import basic.TaskSetInfo;
import utilSim.Log;
import utilSim.MUtil;

public class AnalEDF_AT extends Anal {
	private double lotasks_loutil;
	private double hitasks_loutil;
	private double hitasks_hiutil;
	private double glo_x;
	TaskSetInfo g_info;
	@Override
	public void prepare() {
		g_info=tm.getInfo();
		lotasks_loutil=g_info.getLo_util();
		hitasks_loutil=g_info.getHi_util_lm();
		hitasks_hiutil=g_info.getHi_util_hm();
		glo_x=hitasks_loutil/(1-lotasks_loutil);
		Log.prn(1, "util:"+lotasks_loutil+","+hitasks_loutil+","+hitasks_hiutil);
		Log.prn(1, "x:"+glo_x);
	}
	
	@Override
	public double getDtm() {
		double dtm=glo_x*lotasks_loutil;
		for(int i=0;i<g_info.getSize();i++){
			Task t=tm.getTask(i);
			if (!t.is_HI)
				continue;
			double v_util=t.getLoUtil()/glo_x;
			double h_util=t.getHiUtil();
//			Log.prn(1,"v h:"+v_util+","+h_util);
			dtm+=Math.max(v_util,h_util);
		}
		Log.prn(1,"det:"+dtm);
		return dtm;
	}


	private int getNum(double u){
		double ul=0;
//		double ud=0;
		int num=0;
		for(int j=0;j<g_info.getSize();j++){
			Task t=tm.getTask(j);
			if(t.is_HI) 
				continue;
			double add=t.getLoUtil();
			if (ul+add<=1-u+MUtil.err) {
				ul+=add;
			} else {
//				ud+=add;
				num++;
			}
		}
		return num;
		
	}
	
	// no ratio / nl 
	public double getHUtil(int i,double prob_hi){
		Task[] htasks=tm.getHiTasks();
		double u=0;
		double prob=1;
		for(int j=0;j<htasks.length;j++){
			int v=(i&(1<<j))>>j;
			Task t=htasks[j];
			if (v==0){
				u+=t.getLoUtil()/glo_x;
				Log.prnc(1, "- ");
				prob=prob*(1-prob_hi);
			} else {
				u+=t.getHiUtil();
				Log.prnc(1, "+ ");
				prob=prob*(prob_hi);
			}
		}
		int num=getNum(u);
//		if(num>=1) 	num=tm.lo_size();    // test for EDF-VD
		double sum_prob=num*prob;
		Log.prn(1, u+" "+num+" "+prob+" "+sum_prob);
		return sum_prob;
	}
	
	@Override
	public double getDropRate(double p) {
		double sum_prob=0;
		for(int i=0;i<=g_info.getHi_size();i++){
			sum_prob+=1;
			Log.prn(1, i+" "+sum_prob);
		}
//		int lim=(int)Math.pow(2,hi_size);
//		for(int i=0;i<lim;i++){
//			sum_prob+=getHUtil(i,prob_hi);
//		}
		int num=g_info.getLo_size();
		return sum_prob/num;
	}

	public double getX() {
		return glo_x;
	}
}
