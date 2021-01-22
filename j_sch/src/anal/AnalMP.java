package anal;
// based on EDF-AD-E


import util.SLog;
import task.SysInfo;
import task.Task;
import util.MCal;

public class AnalMP extends Anal {
	private double g_lt_lu;
	private double g_lt_lu_max;
	private double g_ht_lu;
	private double g_ht_hu;
	private double g_ft_lu;
	private double g_ft_hu;
	private double glo_x;
	private int n_hi_prefer;
	SysInfo g_info;
	public AnalMP() {
		super();
		g_name="MP";
	}
	
	
	
	@Override
	public void prepare() {
		load();
		comp_X(g_lt_lu);
		comp_hi_prefer();
		comp_lt_lu_max();
		
	}
	
	private void comp_X(double lt_lu) {
		double cal_x=(1-g_ht_hu)/lt_lu;
		glo_x=Math.min(1,cal_x);
//		Log.prn(2, "x:"+glo_x);
		
	}

	private void comp_lt_lu_max() {
		g_lt_lu_max=(1-g_ht_hu)*(1-g_ft_hu)/(1+g_ht_lu-g_ft_lu-g_ht_hu);
//		Log.prn(2, "lo_max:"+g_lt_lu_max);
	}
	
	private void load() {
		g_info=g_tm.getInfo();
		g_lt_lu=g_info.getUtil_LC();
		g_ht_lu=g_info.getUtil_HC_LO();
		g_ht_hu=g_info.getUtil_HC_HI();
	}

	
	private void comp_hi_prefer() {
		n_hi_prefer=0;
		g_ft_lu=0;
		g_ft_hu=0;
		for(Task t:g_tm.getHiTasks()){
			double v_util=t.getLoUtil()/glo_x;
			double h_util=t.getHiUtil();
//			Log.prn(1, v_util+","+h_util);
			if(v_util>=h_util){
				n_hi_prefer++;
				t.setHI_only();
				g_ft_lu+=t.getLoUtil();
				g_ft_hu+=t.getHiUtil();
			}
		}
	}

	@Override
	public double getDtm() {
		if(g_ht_hu>1){
			return 2;
		}
		if(g_lt_lu>1){
			return 2;
		}
		double dtm=g_lt_lu;
		for(Task t:g_tm.getHiTasks()){
			double v_util=t.getLoUtil()/glo_x;
			double h_util=t.getHiUtil();
			dtm+=Math.min(v_util,h_util);
		}
		return dtm;
	}




	

	@Override
	public double computeX() {
		comp_X(g_lt_lu_max);
		return glo_x;
	}
	
	@Override
	public void prn(){
		SLog.prnc(1, "ll:"+MCal.getStr(g_lt_lu));
		SLog.prnc(1, " hl:"+MCal.getStr(g_ht_lu));
		SLog.prn(1, " hh:"+MCal.getStr(g_ht_hu));
		SLog.prnc(1, "x:"+glo_x);
		SLog.prn(1, " hi_prefer:"+n_hi_prefer);
		
	}



	@Override
	public double getExtra(int i) {
		return g_lt_lu_max;
	}
	
	
	
}
