package anal;

import util.SLog;

import java.util.ArrayList;
import java.util.Collections;

import task.SysInfo;
import task.Task;
import util.MCal;
// inverse 
public class AnalEDF_IV5 extends Anal {
	private double g_lt_lu;
	private double g_ht_lu;
	private double g_ht_hu;
	SysInfo g_info;
	public AnalEDF_IV5(String name) {
		super();
		g_name=name;
	}
	public AnalEDF_IV5() {
		super();
		g_name="MC-RUN-BAL";
	}
	@Override
	protected void prepare() {
		load();
	}
	private ArrayList<Double> getDelta(){
		ArrayList<Double> delta=new ArrayList<Double>();
		for(Task t:g_tm.getHiTasks()){
			double l=t.getLoUtil();
			double h=t.getHiUtil();
			double d=compDeriv(h,l,h);
			delta.add(d);
			d=compDeriv(h,l,0);
			delta.add(d);
		}
		delta.add(0.0000);
		Collections.sort(delta);
		
//		for(double d:delta) {
//			SLog.prn(1,"d "+d);
//		}
		
		return delta;
	}
	
	private double getHSum() {
		double dest_z=1.0;
		double step=0.01;
//		g_tm.prn();
//		g_tm.prnInfo();
		double old_hsum=1.0;
		double old_zsum=1.0;
		while(true) {
//			SLog.prn(1,"st:"+step);
//			SLog.prn(1,"dest_z:"+dest_z);
			double hsum=getHSum_ind(dest_z);
			double zsum=getZSum();
			SLog.prn(1,"zs:"+zsum);
			if(hsum>dest_z+MCal.err) {
				dest_z+=step;
				step/=10;
			} else if(zsum>1+MCal.err) {
				dest_z+=step;
				step/=10;
				
			}
			if(step<0.0001)
				break;
			old_hsum=hsum;
			old_zsum=zsum;
			dest_z-=step;
		}
		return getHSum_ind(dest_z);
	}
		
	private double getZSum() {
		double z=g_tm.getLoUtil();
		for(Task t:g_tm.getHiTasks()){
			z+=t.getLoUtil()/(t.vd/t.period);
//			SLog.prn(1,z+" rate "+MCal.getStr(t.getLoUtil())+" "+MCal.getStr(t.vd));			
		}
		return z;
	}
	private double getHSum_ind(double dest_z) {
		ArrayList<Double> delta=getDelta();
		SLog.prn(1,"----- ");
		double old_d=0;
		for(double d:delta) {
//			SLog.prn(1,"d "+d);
			
			double z_sum=g_tm.getLoUtil();
			double h_sum=0;
			for(Task t:g_tm.getHiTasks()){
				double l=t.getLoUtil();
				double h=t.getHiUtil();
				double z=compDtoZ(h,l,d);
				if(z>h)
					z=h;
				double x=l/z;
				double h_r=(h-l)/(1-x);
//				SLog.prn(1,t.tid+" rate "+MCal.getStr(z)+" "+MCal.getStr(h_r));
				h_sum+=h_r;
				z_sum+=z;
			}
//			SLog.prn(1,"z sum "+MCal.getStr(z_sum)+" h sum "+MCal.getStr(h_sum));
			old_d=d;
			if(z_sum<dest_z)
				break;
		}
//		SLog.prn(1,"init d "+MCal.getStr(old_d));
		
		double slack=computeSlack(dest_z,old_d);
//		SLog.prn(1,"s: "+MCal.getStr(slack));
		
		double d_prime=old_d;
		double d_opt=compute_d_opt(d_prime,slack);
//		SLog.prn(1,"d_opt: "+MCal.getStr(d_opt));
		
		double z_sum=setVD(d_opt);
		double h_sum=computeH();
//		showVD();
		errCheck(z_sum,h_sum);
		return h_sum;
	}
	private double computeSlack(double dest_z,double old_d) {
		double slack=dest_z-g_tm.getLoUtil();
		for(Task t:g_tm.getHiTasks()){
			double l=t.getLoUtil();
			double h=t.getHiUtil();
			double z=compDtoZ(h,l,old_d);
			if(z>h)
				z=h;
			slack-=z;
		}
		return slack;
	}

	private double computeH() {
		double h_sum=0;
		for(Task t:g_tm.getHiTasks()){
			double x=t.vd/t.period;
			double l=t.getLoUtil();
			double h=t.getHiUtil();
			double h_r=(h-l)/(1-x);
			h_sum+=h_r;
		}
		return h_sum;
	}
	private double setVD(double d_opt) {
		double z_sum=g_tm.getLoUtil();
		for(Task t:g_tm.getHiTasks()){
			double l=t.getLoUtil();
			double h=t.getHiUtil();
			double z=compDtoZ(h,l,d_opt);
			if(z>h) {
				z=h;
			}
			double x=l/z;
			t.setX(x);
			z_sum+=z;
		}
		return z_sum;
	}
	
	private void showVD() {
		for(Task t:g_tm.getHiTasks()){
			SLog.prn(1,"vd "+MCal.getStr(t.vd)+" "+t.period);
		}
		
	}
	private void errCheck(double z_sum,double h_sum) {
		SLog.prn(1,"z_sum, h_sum: "+MCal.getStr(z_sum)+","+MCal.getStr(h_sum));
		if(z_sum>1+MCal.err) {
//			g_tm.prnTxt();
			g_tm.prnPara();
			SLog.err("z_sum:"+z_sum+" h_sum:"+h_sum);
			
		}
		
	}
	
	private double compute_d_opt(double d_prime, double slack) {
		double alpha=0;
		for(Task t:g_tm.getHiTasks()){
			double l=t.getLoUtil();
			double h=t.getHiUtil();
			double z=compDtoZ(h,l,d_prime);
			//SLog.prn(1,"z h: "+z+" "+h);
			if(z+MCal.err>h)
				continue;
			alpha+=Math.sqrt(l*(h-l));
		}
		//SLog.prn(1,"alpha: "+alpha);
		double temp=alpha/(alpha/Math.sqrt(d_prime)+slack);
		double ret=Math.pow(temp, 2);
		return ret;
	}
	private double compDeriv(double h,double l,double z) {
		return (1-z)*(h-l)/Math.pow(z-l,2);
	}
	
	private double compDtoZ(double h,double l,double d) {
		return Math.sqrt(l*(h-l)/d)+l;
	}


	private void load() {
		g_info=g_tm.getInfo();
		g_lt_lu=g_info.getUtil_LC();
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
			for(Task t:g_tm.getHiTasks()){
				t.setHI_only();
			}
			return g_lt_lu+g_ht_hu;
		}
		double dtm=getHSum();
//		getDtm2();
		return dtm;
	}



	public double getDtm2() {
		double lsum=g_lt_lu;
		for(Task t:g_tm.getHiTasks()){
			lsum+=t.getLoVdUtil();
		}
		SLog.prn(2, "l_sum22:"+lsum);
		double hsum=g_ht_hu;
		double x=0;
		for(Task t:g_tm.getHiTasks()){
			double tempx=t.vd/t.period;
			SLog.prn(2, "x11:"+tempx);

			x=Math.max(x,tempx);
		}
		hsum+=x*g_lt_lu;
		SLog.prn(2, "x22:"+x);
		SLog.prn(2, "h_sum22:"+hsum);
		return 0;
		
	}


	

	@Override
	public void prn() {
		SLog.prnc(2, "ll:"+MCal.getStr(g_lt_lu));
		SLog.prnc(2, " hl:"+MCal.getStr(g_ht_lu));
		SLog.prn(2, " hh:"+MCal.getStr(g_ht_hu));
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
