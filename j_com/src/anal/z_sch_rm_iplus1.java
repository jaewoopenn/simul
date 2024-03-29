package anal;



import com.PRM;

import sample.TS1;
import task.TaskSet;
import util.SLog;
import util.SEngineT;

public class z_sch_rm_iplus1 {
//	public static int idx=1;
	public static int idx=2;
//	public static int idx=3;
	public static int log_level=1;
//	public static int log_level=2;

	
	public int test1()
	{
//		TaskSet tm=TS1.tm1();
		TaskSet tm=TS1.tm2();
		int p=3;
		int i=1;
		int t=9;
		AnalRM_iplus a=new AnalRM_iplus();
		a.init(tm);
		double req=4;
		double exec=a.getExec(p,i,t);
		String st="exec:"+exec;
		SLog.prn(2,st );
		// verification prm p,exec sbf_prm(t=5)
		PRM prm=new PRM(p,exec);
		st="req:"+tm.computeRBF(i, t);
		st="req:"+req;
		
		st+=" sup:"+prm.sbf_i(t);
		SLog.prn(2,st );
		
		return 0;
	}
	public int test2() 
	{
		PRM p=new PRM(3,1.6);
		int end_t=18;
		for(int t=0;t<end_t;t++) {
			String st="t:"+t;
			st+=" sup:"+p.sbf_d(t);
			st+=" sup_i:"+p.sbf_i(t);
			SLog.prn(2,st );
		}
		return 0;
	}
	public  int test3()
	{
		int p=47;
//		int t=10;
//		double req=3;
//		int t=16;
//		double req=7;
//		int t=320;
//		double req=128;
		int t=512;
		double req=319;
		AnalRM_iplus a=new AnalRM_iplus();
		double exec=a.getExecReq(p,t,req);
		String st="exec:"+exec;
		SLog.prn(2,st );
		// verification prm p,exec sbf_prm(t=5)
		PRM prm=new PRM(p,exec);
		st="req:"+req;
		
		st+=" sup:"+prm.sbf_i(t);
		SLog.prn(2,st );
		AnalRM a2=new AnalRM();
		exec=a2.getExecReq(p,t,req);
		st="exec:"+exec;
		SLog.prn(2,st );
		
		return 0;
	}
	public  int test4() {
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
		Class c = z_sch_rm_iplus1.class;
		z_sch_rm_iplus1 m=new z_sch_rm_iplus1();
		int[] aret=z_sch_rm_iplus1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={0,0,0,0,0,0,0,0,0,0};

}
