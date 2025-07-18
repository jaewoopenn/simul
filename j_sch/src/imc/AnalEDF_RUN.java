package imc;

import util.SLog;

import java.util.ArrayList;
import java.util.Collections;

import anal.Anal;
import task.SysInfo;
import task.Task;
import util.MCal;
// inverse 
public class AnalEDF_RUN extends Anal {
	private double g_lt_lu;
	private double g_lt_hu;
	private double g_ht_lu;
	private double g_ht_hu;
	SysInfo g_info;
	public AnalEDF_RUN() {
		super();
		g_name="IMC-PnG"; 
	}
	public AnalEDF_RUN(String n) {
		super();
		g_name=n; 
	}

	@Override
	public void prepare() {
		load();
	}
	private ArrayList<Double> getDelta(){
		ArrayList<Double> delta=new ArrayList<Double>();
		for(Task t:g_tm.get_HC_Tasks()){
			double l=t.getLoUtil();
			double h=t.getHiUtil();
			double d=compDeriv(h,l,h);
			delta.add(d);
			d=compDeriv(h,l,0);
			delta.add(d);
		}
		Collections.sort(delta);

		for(double d:delta) {
			SLog.prn(1,"d "+d);
		}
		
		return delta;
	}
	
	
	private double getHSum() {
//		g_tm.prnInfo();
		
		ArrayList<Double> delta=getDelta();
		double old_d=0;
		for(double d:delta) {
			SLog.prn(1,"d "+d);
			
			double z_sum=g_lt_hu;
			for(Task t:g_tm.get_HC_Tasks()){
				double l=t.getLoUtil();
				double h=t.getHiUtil();
				double z=compDtoZ(h,l,d);
				if(z<h)
					z=h;
//				SLog.prn(1,t.tid+" rate "+(l/(1-(h-l)/z))+" "+z);
				z_sum+=z;
			}
			SLog.prn(1,"z sum "+z_sum);
			if(z_sum>1)
				break;
			old_d=d;
		}
		SLog.prn(1,"init d "+MCal.getStr(old_d));

		
		double slack=computeSlack(old_d);
//		SLog.prn(1,"s: "+MCal.getStr(slack));
		
		double d_prime=old_d;
		double d_opt=compute_d_opt(d_prime,slack);
//		SLog.prn(1,"d_opt: "+MCal.getStr(d_opt));
		
		double z_sum=setVD(d_opt);
		double l_sum=computeL();
//		showVD();
		errCheck(l_sum,z_sum);
		return l_sum;
	}
	
	private double computeSlack(double old_d) {
		double slack=1-g_lt_hu;
		for(Task t:g_tm.get_HC_Tasks()){
			double l=t.getLoUtil();
			double h=t.getHiUtil();
			double z=compDtoZ(h,l,old_d);
			if(z<h)
				z=h;
			slack-=z;
		}	
		return slack;
	}

	private double computeL() {
		double sum=g_tm.getLoUtil();
		for(Task t:g_tm.get_HC_Tasks()){
			double x=t.vd/t.period;
			double l=t.getLoUtil();
			double l_r=l/x;
			sum+=l_r;
		}
		return sum;
	}
	private double setVD(double d_opt) {
		double z_sum=g_lt_hu;
		for(Task t:g_tm.get_HC_Tasks()){
			double l=t.getLoUtil();
			double h=t.getHiUtil();
			double z=compDtoZ(h,l,d_opt);
			if(z<h) {
				z=h;
			}
			double x=1-(h-l)/z;
			t.setX(x);
			double l_r=l/x;
			SLog.prn(1,t.tid+" rate "+l_r+" "+z);
			z_sum+=z;
		}
		return z_sum;
	}
	
	@SuppressWarnings("unused")
	private void showVD() {
		for(Task t:g_tm.get_HC_Tasks()){
			SLog.prn(1,"vd "+MCal.getStr(t.vd)+" "+t.period);
		}
		
	}
	private void errCheck(double l_sum,double z_sum) {
		SLog.prn(1,"l_sum, z_sum: "+MCal.getStr(l_sum)+","+MCal.getStr(z_sum));
		if(z_sum>1+MCal.err) {
//			g_tm.prnTxt();
			g_tm.prnPara();
			SLog.err("z_sum:"+l_sum+" h_sum:"+z_sum);
			
		}
		
	}
	
	private double compute_d_opt(double d_prime, double slack) {
		double alpha=0;
		for(Task t:g_tm.get_HC_Tasks()){
			double l=t.getLoUtil();
			double h=t.getHiUtil();
			double z=compDtoZ(h,l,d_prime);
			SLog.prn(1,"z h: "+z+" "+h);
			if(z+MCal.err<h)
				continue;
			alpha+=Math.sqrt(l*(h-l));
		}
		SLog.prn(1,"alpha: "+alpha);
		double temp=alpha/(alpha/Math.sqrt(-d_prime)+slack);
		double ret=-Math.pow(temp, 2);
		return ret;
	}
	private double compDeriv(double h,double l,double z) {
		return -l*(h-l)/Math.pow(z-h+l,2);
	}
	private double compDtoZ(double h,double l,double d) {
		return Math.sqrt(-l*(h-l)/d)+h-l;
	}

	private void load() {
		g_info=g_tm.getInfo();
		g_lt_lu=g_info.getUtil_LC();
		g_lt_hu=g_info.getUtil_DeLC();
		g_ht_lu=g_info.getUtil_HC_LO();
		g_ht_hu=g_info.getUtil_HC_HI();
	}

	
	
	@Override
	public double getDtm() {
		if(g_ht_hu>1){
			return g_ht_hu;
		}
		if(g_lt_lu>1){
			return g_lt_lu;
		}
		if(g_lt_lu+g_ht_hu<=1) {
			for(Task t:g_tm.get_HC_Tasks()){
				t.setHI_only();
			}
			return g_lt_lu+g_ht_hu;
		}
		double dtm=getHSum();
		return dtm;
	}





	

	@Override
	public void prn() {
		SLog.prnc(2, "ll:"+MCal.getStr(g_lt_lu));
		SLog.prnc(2, " hl:"+MCal.getStr(g_ht_lu));
		SLog.prnc(2, " hh:"+MCal.getStr(g_ht_hu));
		SLog.prn(2, " lh:"+MCal.getStr(g_lt_hu));
		SLog.prn(2, "det:"+getDtm());
		g_tm.prnTxt();
		
	}
	@Override
	public double getExtra(int i) {
		return 0;
	}
	
	@Override
	public double computeX() {
		return -2;
	}
}
