package imc;

import anal.Anal;
import anal.AnalEDF_VD_ADM;
import anal.AnalEDF_VD_IMC;
import gen.SysLoad;
import sim.*;
import task.DTaskVec;
import util.SEngineT;
import util.SLog;
import util.SLogF;

@SuppressWarnings("unused")
public class z_tasksimul2 {
//	public static int idx=1;
	public static int idx=2;
//	public static int log_level=1;
	public static int log_level=2;


	
	public int test1()	{
		String tsn="adm/test1/taskset_75.txt";
		String out="adm/test.txt";
		int n=3;
		SysLoad sy=new SysLoad(tsn);
		String ret=sy.open();
//		SLog.prn(1, ret);
		sy.moveto(n);
		DTaskVec dt=sy.loadOne2();
		
		simul(dt,out);
		return 0;
	}
	public int test2() {
		String tsn="adm/test1/taskset_75.txt";
		String out="adm/test.txt";
		SysLoad sy=new SysLoad(tsn);
		String ret=sy.open();
		for(int i=0;i<10;i++) {
			DTaskVec dt=sy.loadOne2();
			SLog.prnc(2, i+": ");
			simul(dt,out);
		}
		return 0;
	}
	
	private void simul(DTaskVec dt, String out) {
		int dur=10000;
//		Anal a=new AnalEDF_VD_IMC();
		Anal a=new AnalEDF_VD_ADM();
		a.init(dt.getTM(0));
		double x=a.computeX();
//		SLog.prn(2, x);

		SysMng sm=new SysMng();
		sm.setMS_Prob(0.5);
		sm.setX(x);
		
//		TaskSimul_IMC ts=new TaskSimul_EDF_VD_IMC();
		TaskSimul ts=new TaskSimul_EDF_VD_ADM();
		ts.init_sm_dt(sm,dt);

//		SLogF.init(out);
		ts.simul(dur);
		SimulInfo si=ts.getStat();
//		si.prn2();
		SLog.prn(2, si.getDegraded()+"");
//		SLogF.save();
		
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
		Class c = z_tasksimul2.class;
		z_tasksimul2 m=new z_tasksimul2();
		int[] aret=z_tasksimul2.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}