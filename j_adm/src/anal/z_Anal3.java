package anal;

import gen.SysLoad;
import task.DTaskVec;
import task.TaskMng;
import task.TaskSet;
import util.SEngineT;
import util.SLog;
import util.MCal;
import util.MLoop;


@SuppressWarnings("unused")
public class z_Anal3 {
	public static void init_s() {
		s_idx=1;
//		s_idx=2;
//		s_idx=3;
		
		
		s_log_level=1;
	}


	public int test1()	{
		String ts="adm/test1/taskset_77.txt";
		SysLoad sy=new SysLoad(ts);
		int n=sy.getNum();
		Anal a=new AnalEDF_VD_ADM();
		Anal a2=new AnalEDF_VD_IMC();

		for(int i=0;i<n;i++) {
			a.reset();
			a2.reset();
			DTaskVec dt=sy.loadOne();
			int num=dt.getStageNum();
			double dtm=0;
			double x1=-1;
			double x2=-1;
			for(int j=0;j<num;j++) {
				TaskSet tmp=new TaskSet(dt.getVec(j));
				TaskMng tm=tmp.getTM();
//				tm.prn();
				a.init(tm);
				a2.init(tm);
				if(x1==-1) {
					x1=a.computeX();
				}
				if(x2==-1) {
					x2=a2.computeX();
				}
				a.setX(x1);
				a2.setX(x2);
				double d1=a.getDtm();
				double d2=a2.getDtm();
				if(d1>1+MCal.err) {
					SLog.prn(1, "!!");
				}
				SLog.prn(1, i+" dtm:"+MCal.getStr(d1)+","+MCal.getStr(d2));
			}
		}
		
		return 0;
	}
	
	public int test2() {
		return -1;
	}
	public int test3() {
		return -1;
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
		z_Anal3.init_s();
		Class c = z_Anal3.class;
		z_Anal3 m=new z_Anal3();
		int[] aret=z_Anal3.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	private static int s_idx;
	private static int s_log_level;
}