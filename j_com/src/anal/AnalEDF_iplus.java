package anal;

import com.PRM;

import task.Task;
import util.MCal;
import util.SLog;

public class AnalEDF_iplus extends Anal{
	public AnalEDF_iplus() {
		super();
		g_name="Q-CSF";
	}	
	
	@Override
	public boolean is_sch() {
//		return true;
		return checkSch(g_prm);
	}
	
	@Override
	public boolean checkSch(PRM p) {
		long end_t=getLCM();
		return checkSch_in(p,end_t);
	}
	public long getLCM() {
		long lcm=MCal.lcm(g_ts.getPeriods());
//		SLog.prn(3, lcm);
		if(lcm>g_limit)
			lcm=g_limit;
//		SLog.prn(3, lcm);
		return lcm;
	}
	public boolean checkSch_in(PRM p,  long end_t) {
		int log_lv=1;
		SLog.prn(log_lv, " end_t:"+end_t);
		SLog.prn(log_lv, "t \t sup \t req ");
		String st="";
		for(int t=1;t<=end_t;t++) {
			double d=g_ts.computeDBF(t);
			double s=p.sbf_i(t);
			st=t+"\t"+s;
			if (s+g_error<d) {
				st+=" <<<<"+"\t"+d+"\t";
				SLog.prn(log_lv,st );
				return false;
			} else {
				st+=" >"+"\t"+d+"\t";
			}
			SLog.prn(log_lv,st );
		}
		
		return true;
	}
	
	@Override
	public double getExec(int p) {
		long end_t=getLCM();
		double exec=0;
		double old_req=0;
		for(int t=0;t<end_t;t++) {
			double req=g_ts.computeDBF(t);
			if(req==old_req)
				continue;
			old_req=req;
			double exec1=getExec(p,t,req);
			SLog.prn(1,"t:"+t+" "+exec1+" "+exec );
			exec=Math.max(exec,exec1);
			
		}
		exec=Math.max(g_ts.getUtil()*p,exec);
		return exec;
	}
	public double getExec(int pi, int t, double req) {
		if(req==0)
			return 0;
		int kp2=t/pi;
		int kp1=kp2-1;
		if (kp2<0)
			kp2=0;
		String st="";
		st+="t:"+t;
		st+=" req:"+req;
		st+=" kp1:"+kp1;
		st+=" kp2:"+kp2;
		SLog.prn(1, st);
		st="Compute Normal";
		SLog.prn(1, st);
		double temp1=getThetaNormal(req,pi,t,kp1);
		double temp2=getThetaNormal(req,pi,t,kp2);
		double thetaNormal=Math.min(temp1, temp2);
		st="case1:"+temp1;
		st+=" case2:"+temp2;
		st+=" thetaNor:"+thetaNormal;
		SLog.prn(1, st);
		st="Compute Multiple";
		SLog.prn(1, st);
		temp1=getThetaMultiple(req,pi,t,kp1);
		temp2=getThetaMultiple(req,pi,t,kp2);
		double thetaMultiple=Math.min(temp1, temp2);
		st="case1:"+temp1;
		st+=" case2:"+temp2;
		st+=" thetaMul:"+thetaMultiple;
		SLog.prn(1, st);
		double theta=Math.min(thetaNormal, thetaMultiple);
		st="theta Final:"+theta;
		
		SLog.prn(1, st);
		
		return theta;
	}
	
	// compute theta1,  r mod theta !=0
	// alpha > pi-theta
	private  double getThetaNormal(double req, int pi, int t,int k) {
//		if(k==-1)
//			return pi;
		double theta=pi-(t-req-2)/(k+2);
		double init_d=pi-Math.floor(theta);
		double alpha=t-init_d-k*pi;
		String st=" theta:"+theta;
		st+=" init_d:"+init_d;
		st+=" alpha:"+alpha;
		st+=" pi-theta:"+(pi-theta);
		SLog.prn(1, st);
		if(0<=alpha && alpha<=pi)
			return theta;
		return pi;
	}
	
	// compute theta2,  r mod theta ==0
	// alpha <= pi-theta
	private double getThetaMultiple(double req, int pi,  int t,int k) {
		if(k<=0)
			return pi;
		String st="";
		
		double theta=(req+1)/k;
		if(theta>pi)
			return pi;
		st+=" theta:"+theta;
		double init_d=pi-Math.floor(theta);
		st+=" init_d:"+init_d;
		double alpha=t-init_d-k*pi;
		st+=" alpha:"+alpha;
//		SLog.prn(1, st);
		if(0<=alpha && alpha <=pi)
			return theta;
		return pi;
	}


}
	
