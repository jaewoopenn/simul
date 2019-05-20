package anal;

import com.PRM;

import task.Task;
import util.SLog;

public class AnalRM extends Anal{
	public AnalRM() {
		super();
		g_name="RM";
	}	@Override
	public boolean is_sch() {
		return checkSch(g_prm);
	}	

	public boolean checkSch_ind(PRM p, int i, int end_t) {
		int log_lv=1;
		SLog.prn(log_lv, "i:"+i+" end_t:"+end_t);
		SLog.prn(log_lv, "t \t sup \t req ");
		String st="";
		for(int t=1;t<=end_t;t++) {
			double r=MAnal.computeRBF(g_tm.getArr(),i,t);
			double s=p.sbf(t);
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

	public boolean checkSch(PRM p) {
		Task[] tm=g_tm.getArr();
		for(int i=0;i<tm.length;i++) {
			int end_t=tm[i].period;
			if(!checkSch_ind(p,i,end_t))
				return false; // 하나라도 안되면 실패
				
		}
		return true; // 모두 성공하면 OK
	}

	public double getExec(int p) {
		Task[] tm=g_tm.getArr();
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
				SLog.prn(2, st);
				exec1=Math.min(exec1,tempExec);
			}
			exec=Math.max(exec,exec1);
		}		
		return exec;
	}

	public double getExec(int pi, int i, int t) {
		Task[] tm=g_tm.getArr();
		double req=MAnal.computeRBF(tm,i,t);
		int kp1=t/pi;
		int kp2=kp1-1;
		if (kp2<0)
			kp2=0;
		String st="";
		st+="req:"+req;
		st+=" kp1:"+kp1;
		st+=" kp2:"+kp2;
		SLog.prn(1, st);
		double temp1=getTheta1(req,pi,t,kp1);
		double temp2=getTheta1(req,pi,t,kp2);
		double theta1=Math.min(temp1, temp2);
		st="t1:"+temp1;
		st+=" t2:"+temp2;
		st+=" theta1:"+theta1;
		SLog.prn(1, st);
		temp1=getTheta2(req,pi,t,kp1);
		temp2=getTheta2(req,pi,t,kp2);
		double theta2=Math.min(temp1, temp2);
		st="t1:"+temp1;
		st+=" t2:"+temp2;
		st+=" theta2:"+theta2;
		SLog.prn(1, st);
		double theta=Math.min(theta1, theta2);
		st=" theta:"+theta;
		
		SLog.prn(1, st);
		
		return theta;
	}
	// compute theta1,  r mod theta !=0
	private  double getTheta1(double req, int pi,  int t,int k) {
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
	private double getTheta2(double req, int pi,  int t,int k) {
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
	public void prnReq(int i) {
		Task[] tm=g_tm.getArr();

		for(int t=1;t<tm[i].period;t++) {
			double req=MAnal.computeRBF(tm,i,t);
			String st=t+":"+req+" "+req/t;
			SLog.prn(1, st);
		}
		
	}

}
	
