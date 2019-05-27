package anal;



import com.PRM;

import sample.TS1;
import task.TaskSet;
import util.SLog;
import util.SEngineT;

public class z_draw1 {

	public static void init_s() {
//		int s=1;
//		int s=2;
//		int s=3;
		int s=4;
		
//		int log=1;
		int log=2;
		
		s_idx=s;
		s_log_level=log;
	}
	
	public int test1() {
		TaskSet tm=TS1.tm1();
//		TaskSet tm=TS1.tm2();
//		TaskSet tm=TS1.tm3();
		tm.sort();
		String st="";
		PRM prm=new PRM(3,1.4);
		double t=prm.nextPt(0);
		st+=t;
		SLog.prn(2, st);
		return 0;
	}
	public int test2() {
//		TaskSet tm=TS1.tm1();
		TaskSet tm=TS1.tm2();
//		TaskSet tm=TS1.tm3();
		tm.sort();
		int end_t=30;
		String st="";
		double t=0;
		double next_t=0;
		PRM prm=new PRM(3,1.5);
//		int i=0;
		while(t<end_t) {
			st="t:"+t;
			next_t=prm.nextPt(t);
//			st+=" next_t:"+next_t;
			st+=" s:"+prm.sbf_d(t);
			st+=" r:"+tm.computeRBF(2, t);
			SLog.prn(2, st);
			t=next_t;
//			i++;
//			if(i>10) break;
		}
		return 0;
	}
	public  int test3() {
//		TaskSet tm=TS1.tm1();
		TaskSet tm=TS1.tm2();
//		TaskSet tm=TS1.tm3();
		tm.sort();
		int end_t=30;
		double t=0;
		double next_t=0;
		PRM prm=new PRM(3,1.5);
//		int i=0;
		double d=0;
		double old_d=0;
		for(t=0;t<end_t;t++) {
			next_t=prm.nextPt(t);
			d=tm.computeRBF(2, t);
			if(d != old_d) {
				prn2(t,prm,tm);
				old_d=d;
			}
			if(next_t<=t+1) {
				prn(next_t,prm,tm);
			}
		}
		return 0;
	}
	public void prn2(double t,PRM prm, TaskSet tm) {
		prn(t,prm,tm);
		prn(t+0.001,prm,tm);
	}
	public void prn(double t,PRM prm, TaskSet tm) {
		if(t<0.001)
			return;
		String st="";
		st+=t;
		st+=" "+prm.sbf_d(t);
		st+=" "+tm.computeRBF(2, t-0.001);
		SLog.prn(2, st);
	}
	public  int test4() {
		TaskSet tm=TS1.tm2();
		tm.sort();
		PRM prm=new PRM(3,1.4);
		AnalDraw a=new AnalDraw(tm,prm);
		a.make("com/test_g.txt", 2, 30);
		return 1;
	}
	public  int test5() {
		return 1;
	}
	public  int test6() {
		return 1;
	}
	public  int test7() {
		return 1;
	}
	public  int test8() {
		return 0;
	}
	public  int test9() {
		return 0;
	}
	public  int test10() {
		return 0;
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		z_draw1.init_s();
		Class c = z_draw1.class;
		z_draw1 m=new z_draw1();
		int[] aret=z_draw1.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	private static int s_idx;
	private static int s_log_level;
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
