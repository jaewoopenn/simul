package anal;

import com.PRM;

import task.Task;
import util.SLog;

public class AnalRM_iplus extends Anal{
	public AnalRM_iplus() {
		super();
		g_name="Q-CSF";
	}	
	
	@Override
	public boolean is_sch() {
//		return true;
		return checkSch(g_prm);
	}
	public boolean checkSch(PRM p) {
		Task[] tm=g_ts.getArr();
		for(int i=0;i<tm.length;i++) {
			int end_t=tm[i].period;
			if(!checkSch_ind(p,i,end_t))
				return false; // 하나라도 안되면 실패
				
		}
		return true; // 모두 성공하면 OK
	}	
	public boolean checkSch_ind(PRM p, int i, int end_t) {
		int log_lv=1;
		SLog.prn(log_lv, "i:"+i+" end_t:"+end_t);
		SLog.prn(log_lv, "t \t sup \t req ");
		String st="";
		for(int t=1;t<=end_t;t++) {
			double r=g_ts.computeRBF(i,t);
			double s=p.sbf_i(t);
			st=t+"\t"+s+"\t"+r+"\t";
			if (s+g_error>r) {
				st+=">>>>>";
				SLog.prn(log_lv,st );
				return true;
			} else {
				st+="<";
			}
//			SLog.prn(log_lv,st );
		}
		SLog.prn(log_lv,st );
		
		return false;
	}
	@Override
	public double getExec(int p) {
		Task[] tm=g_ts.getArr();
		double exec=0;
		for(int i=0;i<tm.length;i++) {
			int end_t=tm[i].period;
			double exec1=p;
			
			for(int t=1;t<=end_t;t++) {
				double tempExec=getExec(p,i,t);
				String st="";
				st+=i+" "+t;
				st+=" temp:"+tempExec;
				st+=" exec1:"+exec1;
//				if(t==end_t)
					SLog.prn(2, st);
				exec1=Math.min(exec1,tempExec);
			}
			exec=Math.max(exec,exec1);
			String st=i+" ";
			st+=" exec:"+exec;
			SLog.prn(2, st);
		}		
		return exec;
	}
	public double getExec(int pi, int i, int t) {
		double req=g_ts.computeRBF(i,t);
		SLog.prn(2, t+" "+req);
		return getExecReq(pi,t,req);
	}
	public double getExecReq(int pi,int t,double req) {
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
		st="case1:"+temp1;
		SLog.prn(1, st);
		double temp2=getThetaNormal(req,pi,t,kp2);
		double thetaNormal=Math.min(temp1, temp2);
		st=" case2:"+temp2;
		st+=" thetaNor:"+thetaNormal;
		SLog.prn(1, st);
		st="Compute Multiple";
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
		st=" thetaF:"+theta;
		
		SLog.prn(1, st);
		
		return theta;
	}
	
	// compute theta1,  r mod theta !=0
	// alpha > pi-theta
	private  double getThetaNormal(double req, int pi, int t,int k) {
//		if(k==-1)
//			return pi;
		double theta=pi-(t-req-2)/(k+2);
		double init_d=pi-Math.floor(theta+0.5);
		String st=" theta:"+theta;
		st+=" init_d:"+init_d;
		double alpha=t-init_d-k*pi;
		st+=" alpha:"+alpha;
//		st+=" pi-theta:"+(pi-theta);
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
		
		double theta=req/k;
		if(theta>pi)
			return pi;
		st+=" theta:"+theta;
		double init_d=pi-Math.floor(theta+0.5);
		st+=" init_d:"+init_d;
		double alpha=t-init_d-k*pi;
		st+=" alpha:"+alpha;
		SLog.prn(1, st);
		if(0<=alpha && alpha <=pi)
			return theta;
		return pi;
	}


}
	
