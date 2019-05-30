package anal;

import util.SLog;

public class AnalRM_iplus extends Anal{
	public AnalRM_iplus() {
		super();
		g_name="RM_iplus";
	}	
	
	@Override
	public boolean is_sch() {
		AnalRM a=new AnalRM();
		a.init(g_ts,g_prm);
		return a.is_sch();
	}	

	@Override
	public double getExec(int p) {
		AnalRM a=new AnalRM();
		a.init(g_ts);
		double e=a.getExec(p);
		return Math.ceil(e);
	}
	public double getExec(int pi, int i, int t) {
		double req=g_ts.computeRBF(i,t);
		int kp1=t/pi;
		int kp2=kp1-1;
		if (kp2<0)
			kp2=0;
		String st="";
		st+="req:"+req;
		st+=" kp1:"+kp1;
		st+=" kp2:"+kp2;
		SLog.prn(1, st);
		double temp1=getThetaNormal(req,pi,t,kp1);
		st="case1:"+temp1;
		SLog.prn(1, st);
		double temp2=getThetaNormal(req,pi,t,kp2);
		double thetaNormal=Math.min(temp1, temp2);
		st=" case2:"+temp2;
		st+=" thetaNor:"+thetaNormal;
		SLog.prn(1, st);
		temp1=getThetaMultiple(req,pi,t,kp1);
		st="case1:"+temp1;
		SLog.prn(1, st);
		temp2=getThetaMultiple(req,pi,t,kp2);
		double thetaMultiple=Math.min(temp1, temp2);
		st=" case2:"+temp2;
		st+=" thetaMul:"+thetaMultiple;
		SLog.prn(1, st);
		double theta=Math.min(thetaNormal, thetaMultiple);
		st=" theta:"+theta;
		
		SLog.prn(1, st);
		
		return theta;
	}
	
	// compute theta1,  r mod theta !=0
	private  double getThetaNormal(double req, int pi,  int t,int k) {
		double theta=pi-(t-req)/(k+2);
		String st=" th:"+theta;
		double alpha=t-k*pi-(pi-theta);
		st+=" alpha:"+alpha;
		st+=" pi-theta:"+(pi-theta);
		SLog.prn(1, st);
		if(alpha <pi-theta || alpha>pi)
			return pi;
		return theta;
	}
	// compute theta2,  r mod theta ==0
	private double getThetaMultiple(double req, int pi,  int t,int k) {
		if(k==0)
			return pi;
		String st="";
		
		double theta=req/k;
		if(theta>pi)
			return pi;
		st+=" th:"+theta;
		double alpha=t-k*pi-(pi-theta);
		st+=" alpha:"+alpha;
		SLog.prn(1, st);
		if(alpha<0 || alpha >pi-theta)
			return pi;
		return theta;
	}


}
	
