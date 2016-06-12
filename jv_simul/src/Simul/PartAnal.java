package Simul;


import Util.Log;

public class PartAnal  {
	CompMng cm;
	private double g_lt_LU;
	private double g_ht_LU;
	private double g_ht_HU;
	private double g_x;
	private double g_alpha;

	public void init(CompMng mng) {
		cm=mng;
		g_lt_LU=cm.get_lt_LU();
		g_ht_LU=cm.get_ht_LU();
		g_ht_HU=cm.get_ht_HU();
	}
	public void compute_X() {
//		glo_x=hitasks_loutil/(1-lotasks_loutil);
		g_x=Math.min(1,  (1-g_ht_HU)/g_lt_LU);

		Log.prn(1, "util:"+g_lt_LU+","+g_ht_LU+","+g_ht_HU);
		Log.prn(1, "x:"+g_x);
	}
	public double comp_interface_ht_lu(int i)
	{
		TaskMng tm=cm.getComp(i);
		double lt_LU=tm.getLoUtil(); 
		double ht_LU=tm.getHiUtil_l();
		double ht_HU=tm.getHiUtil_h();
		double delta=Math.max(0, g_x*ht_HU-ht_LU
				-g_x*(1-g_x)*lt_LU);
		double re_hitasks_loutil=ht_LU+g_alpha*delta;
		return re_hitasks_loutil;
		
	}

	public double comp_interface_hi(int i)
	{
		TaskMng tm=cm.getComp(i);
		double lt_LU=tm.getLoUtil(); 
		double ht_LU=tm.getHiUtil_l();
		double ht_HU=tm.getHiUtil_h();
		double delta=Math.max(0, g_x*ht_HU-ht_LU
				-g_x*(1-g_x)*lt_LU);
		double re_hitasks_loutil=ht_LU+g_alpha*delta;
		return re_hitasks_loutil;
		
	}
	
	public void comp_interface_help(int i)
	{
		TaskMng tm=cm.getComp(i);
		double lt_LU=tm.getLoUtil(); 
		double ht_LU=tm.getHiUtil_l();
		double ht_HU=tm.getHiUtil_h();
		double lo_det=lt_LU+ht_LU/g_x;
		double hi_det=g_x*lt_LU+ht_HU;
		Log.prn(1, "lo_det:"+lo_det);
		Log.prn(1, "hi_det:"+hi_det);
		double delta=Math.max(0, g_x*ht_HU-ht_LU
				-g_x*(1-g_x)*lt_LU);
		Log.prn(1, "delta:"+delta);
		double re_lo_det=lt_LU+(ht_LU+delta)/g_x;
		Log.prn(1, "re_lo_det:"+re_lo_det);
		double re_hitasks_loutil=ht_LU+g_alpha*delta;
		Log.prn(1, "hitasks_loutil:"+ht_LU);
		Log.prn(1, "re_hitasks_lo_util:"+re_hitasks_loutil
				+", alpha:"+g_alpha);
		Log.prn(1, "strong_hitasks_lo_util:"+(ht_LU+delta));
		
	}
	
	public double get_X() {
		return g_x;
	}
	public void set_X(double d) {
		g_x=d;
	}
	public void set_alpha(double d) {
		g_alpha=d;
		
	}
	public TaskMng getInterfaces() {
		TaskMng ret_tm=new TaskMng();
		for(int i=0;i<cm.getSize();i++){
			TaskMng tm=cm.getComp(i);
			double lt_LU=tm.getLoUtil(); 
			double ht_LU=comp_interface_hi(i);
			double ht_HU=tm.getHiUtil_h();
			ret_tm.addTask(1, lt_LU);
			ret_tm.addHiTask(1, ht_LU,ht_HU);
		}
		ret_tm.freezeTasks();
		return ret_tm;
	}


}
