package anal;



import com.PRM;

import util.SLog;
import util.SEngineT;

public class z_sch_rm_iplus2 {
	public static int idx=1;
//	public static int idx=2;
//	public static int idx=3;
//	public static int log_level=1;
	public static int log_level=2;

	
	public int[] getPara1() {
//		int temp[]= {3,10,3};
//		int temp[]= {3,16,7};
//		int temp[]= {15,320,128};
//		int temp[]= {47,512,319};
//		int temp[]= {3,2,2};
		int temp[]= {25,283,196};
		return temp;
	}
	
	public int test1()
	{
		int temp[]=getPara1();
		int p=temp[0];
		int t=temp[1];
		double req=temp[2];
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
		st="req:"+req;
		
		st+=" sup:"+prm.sbf_d(t);
		SLog.prn(2,st );
		
		return 0;
	}
	public int test2() 
	{
		return 0;
	}
	public  int test3()
	{		
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
		Class c = z_sch_rm_iplus2.class;
		z_sch_rm_iplus2 m=new z_sch_rm_iplus2();
		int[] aret=z_sch_rm_iplus2.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={0,0,0,0,0,0,0,0,0,0};

}
