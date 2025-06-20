package imc;

import gen.SysLoad;
import task.SysInfo;
import sim.*;
import task.DTaskVec;
import task.TaskMng;
import task.TaskSet;
import util.SEngineT;
import util.SLog;
import util.SLogF;

public class z_tasksimul {
	public static int idx=1;
//	public static int idx=2;
	public static int log_level=1;


	public int test1()	{
		int n=25;
		int dur=350;
		double p=0.5;
		double x=0.54;

		String tsn="adm/test1/taskset_80.txt";
		String out="adm/test.log.txt";
//		boolean bSave=true;
		boolean bSave=false;
		
		SysLoad sy=new SysLoad(tsn);
		String ret=sy.open();
		SLog.prn(0, ret);
		sy.moveto(n);
		DTaskVec dt=sy.loadOne2();
		TaskSet its=new TaskSet(dt.getVec(0));
		TaskMng tm=its.getTM();
		tm.prn();
		SysInfo sii=tm.getInfo();
		sii.setX(x);
		SLog.prn(2, "lo sch:"+(sii.getUtil_HC_LO()/x+sii.getUtil_LC()));

		SysMng sm=new SysMng();
		sm.setMS_Prob(p);
		sm.setX(x);
		
//		TaskSimul_IMC ts=new TaskSimul_EDF_VD_IMC();
		TaskSimul_IMC ts=new TaskSimul_EDF_VD_ADM();
		SLog.prn(1, ts.getName());
		ts.init_sm_dt(sm,dt);
//
		if(bSave)	
			SLogF.init(out);
		ts.simul(dur);
		SimulInfo si=ts.getSI();
		si.prn2();
		if(bSave)	
			SLogF.save();
		return 0;
	}
	
	public int test2() {
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
		Class c = z_tasksimul.class;
		z_tasksimul m=new z_tasksimul();
		int[] aret=z_tasksimul.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}