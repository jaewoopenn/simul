package anal;

import util.SLog;

import java.util.ArrayList;
import java.util.Collections;

import task.SysInfo;
import task.Task;
import util.MCal;
// inverse 
public class AnalEDF_IV3 extends Anal {
	private double g_lt_lu;
	private double g_ht_lu;
	private double g_ht_hu;
	SysInfo g_info;
	public AnalEDF_IV3(String name) {
		super();
		g_name=name;
	}
	public AnalEDF_IV3() {
		super();
		g_name="MC-RUN";
	}
	@Override
	protected void prepare() {
		load();
	}
	private double getD() {
		double hsum;
		while(true) {
			SLog.prn(1, "-!!!---");
			double ratio=0;
			double idx=-1;
			for(Task t:g_tm.get_HC_Tasks()) {
				if(t.isHI_Preferred())
					continue;
				double ul=t.getLoUtil();
				double uh=t.getHiUtil();
				SLog.prn(1, ul+","+uh+","+ul/uh);
				if(ul/uh>ratio) {
					ratio=ul/uh;
					idx=t.tid;
				}
			}
			setHiOnlyTask(idx,true);
			hsum=getHSum();
			if(hsum>1) {
				setHiOnlyTask(idx,false);
				break;
			}
				
			if(idx==-1)
				break;
		}
		int no=0;
		for(Task t:g_tm.get_HC_Tasks()) {
			if(t.isHI_Preferred())
				no++;
		}
//		SLog.prnc(2, " ho:"+no);
		return getHSum();
	}
	private void setHiOnlyTask(double idx, boolean b) {
		for(Task t:g_tm.get_HC_Tasks()) {
			if(t.tid==idx) {
				if(b)
					t.setHI_only();
				else
					t.setNormal();
			}
		}
		
	}
	private double getHSum() {
		ArrayList<Double> delta=new ArrayList<Double>();
		for(Task t:g_tm.get_HC_Tasks()){
			if(t.isHI_Preferred())
				continue;
			double l=t.getLoUtil();
			double h=t.getHiUtil();
			double d=compDeriv(h,l,h);
			delta.add(d);
			d=compDeriv(h,l,0);
			delta.add(d);
		}
		delta.add(0.0000);
		Collections.sort(delta);

		for(double d:delta) {
			SLog.prn(1,"d "+d);
		}
		SLog.prn(1,"----- ");
		
		double old_d=0;
		double z_sum=0;
		double h_sum=0;
		for(double d:delta) {
			SLog.prn(1,"d "+d);
			
			z_sum=g_tm.getLoUtil();
			h_sum=0;
			for(Task t:g_tm.get_HC_Tasks()){
				double l=t.getLoUtil();
				double h=t.getHiUtil();
				double z=compDtoZ(h,l,d);
				if(z>h)
					z=h;
				double h_r=(h-l)/(1-l/z);
				if(t.isHI_Preferred()) {
					z=h;
					h_r=h;
				}
				SLog.prn(1,t.tid+" rate "+z+" "+h_r);
				z_sum+=z;
				h_sum+=h_r;
			}
			SLog.prn(1,"z_sum:"+z_sum+" h_sum:"+h_sum);
			old_d=d;
			if(z_sum<1)
				break;
		}
		if(z_sum>1)
			return 1.1;
		SLog.prn(1,"init d "+old_d);
		return compute_slack(old_d);
	}
	private double compute_slack(double old_d) {
		double z_sum=0;
		double h_sum=0;
		double slack=1-g_tm.getLoUtil();
		for(Task t:g_tm.get_HC_Tasks()){
			double l=t.getLoUtil();
			double h=t.getHiUtil();
			double z=compDtoZ(h,l,old_d);
			if(z>h)
				z=h;
			if(t.isHI_Preferred()) 
				z=h;
			slack-=z;
		}
		double d_prime=old_d;
		SLog.prn(1,"s: "+slack);
		if(slack<0&&old_d==0) 
			return 1.1;
	
		double d_opt=compute_d_opt(d_prime,slack);
		SLog.prn(1,"d_opt: "+d_opt);
		z_sum=g_tm.getLoUtil();
		h_sum=compute_h_sum(z_sum,d_opt);
		return h_sum;
	}
	private double compute_h_sum(double z_sum,double d) {
		double h_sum=0;
		for(Task t:g_tm.get_HC_Tasks()){
			double l=t.getLoUtil();
			double h=t.getHiUtil();
			double z=compDtoZ(h,l,d);
			if(z>h) {
				z=h;
			}
			if(t.isHI_Preferred()) 
				z=h;
			double x=l/z;
			t.setX(x);
			double h_r=(h-l)/(1-x);
			SLog.prn(1,t.tid+" rate "+z+" "+h_r);
			h_sum+=h_r;
			z_sum+=z;
		}
		SLog.prn(1,"z_sum, h_sum: "+z_sum+","+h_sum);
		for(Task t:g_tm.get_HC_Tasks()){
			SLog.prn(1,"vd "+t.vd+" "+t.period);
		}
		if(z_sum>1+MCal.err) {
//			g_tm.prnTxt();
			g_tm.prnPara();
			SLog.err("z_sum, h_sum: "+z_sum+","+h_sum);
			
		}
		return h_sum;
	}
	
	private double compute_d_opt(double d_prime, double slack) {
		double alpha=0;
		for(Task t:g_tm.get_HC_Tasks()){
			double l=t.getLoUtil();
			if(t.isHI_Preferred()) 
				l=t.getHiUtil();
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
			for(Task t:g_tm.get_HC_Tasks()){
				t.setHI_only();
			}
			return g_lt_lu+g_ht_hu;
		}
		double dtm=getD();
		return dtm;
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
