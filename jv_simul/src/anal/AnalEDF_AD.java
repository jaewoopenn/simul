package anal;

import basic.Task;
import basic.TaskSetInfo;
import util.Log;
import util.MUtil;

public class AnalEDF_AD extends Anal {
	private double lotasks_loutil;
	private double hitasks_loutil;
	private double hitasks_hiutil;
	private double glo_x;
	TaskSetInfo g_info;
	public AnalEDF_AD() {
		super();
		name="AT";
	}
	@Override
	public void prepare() {
		g_info=g_tm.getInfo();
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
		for(Task t:g_tm.getTasks()){
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
		for(Task t:g_tm.getTasks()){
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
		Task[] htasks=g_tm.getHiTasks();
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
	

	public double getX() {
		return glo_x;
	}
}
