package anal;

import gen.SysLoad;
import task.DTaskVec;
import task.TaskMng;
import task.TaskSet;
import util.SEngineT;
import util.SLog;

public class z_Anal2 {
//	public static int idx=1;
	public static int idx=2;
//	public static int idx=3;
	public static int log_level=1;


	public int test1()	{
		String ts="adm/test1/taskset_96";
		int n=1745;
		SysLoad sy=new SysLoad(ts);
		String ret=sy.open();
		SLog.prn(1, ret);
		sy.moveto(n);
		TaskMng tm=sy.loadOne();
		tm.prn();
		Anal a=new AnalAMC_imc();
		a.init(tm);
		SLog.prn(1, a.getDtm());
//		a.prn();

		
		return 0;
	}
	
	public int test2() {
		String ts="adm/test1/taskset_92";
		SysLoad sy=new SysLoad(ts);
		String ret=sy.open();
		SLog.prn(1, ret);
		int n=Integer.valueOf(ret).intValue();
		int s=0;
//		Anal a=new AnalAMC_imc();
//		Anal a=new AnalEDF_VD_IMC();
		Anal a=new AnalEDF_VD_ADM();
		for(int i=0;i<n;i++) {
			a.reset();
			DTaskVec dt=sy.loadOne2();
			int num=dt.getNum();
			double dtm=0;
			for(int j=0;j<num;j++) {
				TaskSet tmp=new TaskSet(dt.getVec(j));
				TaskMng tm=tmp.getTM();
//				tm.prn();
				a.init(tm);
//				a.prn();
				dtm=Math.max(dtm, a.getDtm());
				SLog.prn(1, i+": "+dtm);
			}
			if(dtm<=1)
				s++;
//			tm.prn();
		}
		SLog.prn(1, s+" Suc: "+(double)s/n);
//		a.prn();
		return -1;
	}
	public int test3() {
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
		Class c = z_Anal2.class;
		z_Anal2 m=new z_Anal2();
		int[] aret=z_Anal2.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}