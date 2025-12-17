package anal;

import gen.SysLoad;
import sim.DTUtil;
import task.DTaskVec;
import task.TaskMng;
import task.TaskSet;
import task.TaskUtil;
import util.MLoop;
import util.SEngineT;
import util.SLog;

public class z_Anal2 {
	public static void init_s() {
		s_idx=1;
//		s_idx=2;
//		s_idx=3;
		
		
		s_log_level=1;
	}
	public int test1()	{
		String ts="adm/test1/taskset_68.txt";
//		String ts="adm/test1/test_task.txt";
		SysLoad sy=new SysLoad(ts);
		int num=sy.getNum();
		for(int i:MLoop.on(num)) {
			DTaskVec tm=sy.loadOne();
			SLog.prn(1, "task set "+i);
			anal(tm);
		}
		return 0;
	}
	
	public int test2()	{
		int idx=29;
		String ts="adm/test1/taskset_76.txt";
		SysLoad sy=new SysLoad(ts);
		sy.moveto(idx);
		
		DTaskVec tm=sy.loadOne();
		SLog.prn(1, "task set "+idx);
		anal(tm);
		
		return 0;
	}

	
	private void anal(DTaskVec tm) {
		Anal a;
//		a=new AnalAMC_imc();
		a=new AnalEDF_VD_ADM();
//		a=new AnalEDF_VD_IMC();
		a.init(DTUtil.getCurTM(tm));
		double x=a.computeX();
		SLog.prn(1, "x: "+x);
//		SLog.prn(1, a.getDtm());
//		a.prn();
//		if(x==0) {
//			SLog.err("STOP");
//		}
		
	}


	public int test3() {
		String ts="adm/test2/taskset_98.txt";
//		String ts="adm/test_task.txt";
		SysLoad sy=new SysLoad(ts);
		int n=sy.getNum();
		int s=0;
//		Anal a=new AnalAMC_imc();
//		Anal a=new AnalEDF_VD_IMC();
		Anal a=new AnalEDF_VD_ADM();
//		Anal a=new AnalEDF_BV();
		for(int i=0;i<n;i++) {
			a.reset();
			DTaskVec dt=sy.loadOne();
			int num=dt.getStageNum();
			double dtm=0;
			double x=-1;
			for(int j=0;j<num;j++) {
				TaskSet tmp=new TaskSet(dt.getVec(j));
				TaskMng tm=tmp.getTM();
//				tm.prn();
				a.init(tm);
				if(x==-1) {
					x=a.computeX();
				}
				a.setX(x);
//				a.prn();
				double d=a.getDtm();
				SLog.prn(1, "x: "+x);
				SLog.prn(1, i+": "+d);
				if(d>1) {
					x=a.computeX();
					SLog.prn(1, "re x: "+x);
					a.setX(x);
					d=a.getDtm();
					SLog.prn(1, "re: "+d);
				}
				dtm=Math.max(dtm, d);
			}
			if(dtm<=1)
				s++;
//			tm.prn();
		}
		SLog.prn(1, s+" Suc: "+(double)s/n);
//		a.prn();
		return -1;
	}
	public int test4() {
		String ts="adm/test2/taskset_98.txt";
//		String ts="adm/test_task.txt";
		SysLoad sy=new SysLoad(ts);
//		Anal a=new AnalEDF_VD_ADM();
		Anal a=new AnalEDF_BV();
		DTaskVec dt=sy.loadOne();
		int num=dt.getStageNum();
		double x=-1;
		for(int j=0;j<num;j++) {
			TaskSet tmp=new TaskSet(dt.getVec(j));
			TaskMng tm=tmp.getTM();
			TaskUtil.prn(tm);
			tm.getInfo().prnUtil();
			a.init(tm);
			if(x==-1) {
				x=a.computeX();
			}
			a.setX(x);
			double d=a.getDtm();
			SLog.prn(1, "x: "+x);
			SLog.prn(1, "d: "+d);
			if(d>1) {
				x=a.computeX();
				SLog.prn(1, "re x: "+x);
				a.setX(x);
				d=a.getDtm();
				SLog.prn(1, "re: "+d);
			}
		}
		return -1;
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
		z_Anal2.init_s();
		Class c = z_Anal2.class;
		z_Anal2 m=new z_Anal2();
		int[] aret=z_Anal2.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	private static int s_idx;
	private static int s_log_level;
}