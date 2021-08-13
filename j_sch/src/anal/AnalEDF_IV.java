package anal;

import util.SLog;
import task.SysInfo;
import task.Task;
import task.TaskMng;
import util.MCal;

public class AnalEDF_IV extends Anal {
	private double g_lt_lu;
	private double g_ht_lu;
	private double g_ht_hu;
	private double glo_x;
	private int n_hi_prefer;
	SysInfo g_info;
	public AnalEDF_IV(String name) {
		super();
		g_name=name;
	}
	public AnalEDF_IV() {
		super();
		g_name="MC-IndVD";
	}
	@Override
	public void prepare() {
		load();
		comp_X();
	}
	
	private void comp_X() {
		// delta_zi 
		for(Task t:g_tm.getHiTasks()){
			double l=t.getLoUtil();
			double h=t.getHiUtil();
			SLog.prn(1,h+" "+l);
			for(int i=0;i<10;i++) {
				double z=1-0.01*i;
				double delta=compDeriv(h,l,z);
				SLog.prn(1,"derivate "+z+" "+delta);
//				SLog.prn(1,"h rate "+z+" "+h/z);
//				SLog.prn(1,"l rate "+z+" "+comp_lrate(h,l,z));
//				SLog.prn(1,"delta "+z+" "+h/z/comp_lrate(h,l,z));
			}
//			SLog.prn(1,"h rate 1.00 "+h);
//			SLog.prn(1,"h rate 0.95 "+h/0.95);
//			SLog.prn(1,"h rate 0.90 "+h/0.9);
//			SLog.prn(1,"l rate 1.00 "+comp_lrate(h,l,1));
//			SLog.prn(1,"l rate 0.95 "+comp_lrate(h,l,0.95));
//			SLog.prn(1,"l rate 0.90 "+comp_lrate(h,l,0.90));
//			double x,y;
//			y=h/1 / comp_lrate(h,l,1);
//			x=h/0.95/comp_lrate(h,l,0.95);
//			SLog.prn(1,"delta "+y+" "+x+" "+(y-x));
//			y=h/0.95 / comp_lrate(h,l,0.95);
//			x=h/0.90/comp_lrate(h,l,0.90);
//			SLog.prn(1,"delta "+y+" "+x+" "+(y-x));
		}
		
	}
	private double compDeriv(double h,double l,double z) {
		return -h/l/z/z;
	}
	private double comp_lrate(double h,double l,double z) {
		return h*l/(h-(h-l)*z);
	}

	private void load() {
		g_info=g_tm.getInfo();
		g_lt_lu=g_info.getUtil_LC();
		g_ht_lu=g_info.getUtil_HC_LO();
		g_ht_hu=g_info.getUtil_HC_HI();
	}

	
	private void comp_hi_prefer() {
		n_hi_prefer=0;
		for(Task t:g_tm.getHiTasks()){
			double v_util=t.getLoUtil()/glo_x;
			double h_util=t.getHiUtil();
//			Log.prn(1, v_util+","+h_util);
			if(v_util>=h_util){
				t.setHI_only();
				n_hi_prefer++;
			}
		}
	}
	
	@Override
	public double getDtm() {
		if(g_ht_hu>1){
			return g_ht_hu;
		}
		if(g_lt_lu>1){
			return g_lt_lu;
		}
		double dtm=g_lt_lu;
		for(Task t:g_tm.getHiTasks()){
			double v_util=t.getLoUtil()/glo_x;
			double h_util=t.getHiUtil();
			dtm+=Math.min(v_util,h_util);
		}
		return dtm;
	}




	

	public double computeX() {
		return glo_x;
	}
	public static double computeX(TaskMng tm) {
		AnalEDF_IV a=new AnalEDF_IV();
		a.init(tm);
		a.prepare();
		return a.computeX();
	}
	
	@Override
	public void prn() {
		SLog.prnc(1, "ll:"+MCal.getStr(g_lt_lu));
		SLog.prnc(1, " hl:"+MCal.getStr(g_ht_lu));
		SLog.prn(1, " hh:"+MCal.getStr(g_ht_hu));
		SLog.prnc(1, "x:"+glo_x);
		SLog.prn(1, " hi_prefer:"+n_hi_prefer);
		SLog.prn(1, "det:"+getDtm());
		
	}
	@Override
	public double getExtra(int i) {
		return 0;
	}
}
