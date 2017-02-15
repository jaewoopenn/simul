package anal;

import basic.Task;
import basic.TaskMng;
import basic.TaskSetInfo;
import utill.Log;
import utill.MUtil;

public class AnalEDF_AD_E extends Anal {
	private double lotasks_loutil;
	private double hitasks_loutil;
	private double hitasks_hiutil;
	private double glo_x;
	private int n_skip;
	TaskSetInfo g_info;
	public AnalEDF_AD_E() {
		super();
		name="AT-S";
	}
	@Override
	public void prepare() {
		g_info=tm.getInfo();
		lotasks_loutil=g_info.getLo_util();
		hitasks_loutil=g_info.getHi_util_lm();
		hitasks_hiutil=g_info.getHi_util_hm();
		double cal_x=(1-hitasks_hiutil)/lotasks_loutil;
		glo_x=Math.min(1,cal_x);
		n_skip=0;
		for(Task t:tm.getHiTasks()){
			double v_util=t.getLoUtil()/glo_x;
			double h_util=t.getHiUtil();
//			Log.prn(1, v_util+","+h_util);
			if(v_util>=h_util)
				n_skip++;
		}
		Log.prnc(1, "ll:"+MUtil.getStr(lotasks_loutil));
		Log.prnc(1, " hl:"+MUtil.getStr(hitasks_loutil));
		Log.prn(1, " hh:"+MUtil.getStr(hitasks_hiutil));
		Log.prnc(1, "x:"+glo_x);
		Log.prn(1, "n_skip:"+n_skip);
	}
	
	@Override
	public double getDtm() {
		double dtm=lotasks_loutil;
		for(Task t:tm.getHiTasks()){
			double v_util=t.getLoUtil()/glo_x;
			double h_util=t.getHiUtil();
//			Log.prn(1,"v h:"+v_util+","+h_util);
			dtm+=Math.min(v_util,h_util);
		}
		Log.prn(1,"det:"+dtm);
		return dtm;
	}




	
	@Override
	public double getDropRate(double p) {
		double exp_drop_sum=0;
		int drop=0;
		double exp_drop=0;
		int nf=g_info.getHi_size()-n_skip;
		for(int i=0;i<=nf;i++){
			drop=MUtil.combi(nf, i)*maxDrop(i);
			exp_drop=Math.pow(1-p, i)*Math.pow(p, nf-i)*drop;
//			Log.prn(1, i+" "+drop+" "+exp_drop);
			exp_drop_sum+=exp_drop;
		}
		int num=g_info.getLo_size();
		return exp_drop_sum/num;
	}
	private int maxDrop(int k){
		double req_util=getReq(k);
		double lo_util=lotasks_loutil;
		int drop=0;
		for(int i=0;i<g_info.getSize();i++){
			Task t=tm.getTask(i);
			if (t.is_HI)
				continue;
			if(req_util+lo_util>1){
				drop++;
				lo_util-=(1-glo_x)*t.getLoUtil();
			}
			else
				break;
			
		}
//		Log.prn(1, "__ "+k+" "+drop+" "+req_util+" "+lo_util);
		
		return drop;
	}
	private double getReq(int k){
		int nf=g_info.getHi_size()-n_skip;
		double req_util=0;
		int cur=0;
		
		for(Task t:tm.getHiTasks()){
			double v_util=t.getLoUtil()/glo_x;
			double h_util=t.getHiUtil();
			if(v_util>=h_util)
				req_util+=h_util;
			else {
				if (cur<nf-k) {
					req_util+=h_util;
					cur++;
				} else {
					req_util+=v_util;
				}
			}
		}
		
		
		return req_util;
	}

	public double getX() {
		return glo_x;
	}
	public static double computeX(TaskMng tm) {
		AnalEDF_AD_E a=new AnalEDF_AD_E();
		a.init(tm);
		a.prepare();
		return a.getX();
	}
}
