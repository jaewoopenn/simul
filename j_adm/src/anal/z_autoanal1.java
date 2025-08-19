package anal;

import gen.SysLoad;
import sim.AutoSimul;
import sim.DoSimul;
import task.DTaskVec;
import task.TaskMng;
import task.TaskSet;
import util.SEngineT;
import util.SLog;

public class z_autoanal1 {
//	public static int idx=1;
//	public static int idx=2;
	public static int idx=3;
	public static int log_level=1;


	public int test1()	{
		int idx=29;
		String ts="adm/test1/taskset_76.txt";
		SysLoad sy=new SysLoad(ts);
		sy.open();
		sy.moveto(idx);
		
		DTaskVec tm=sy.loadOne2();
		SLog.prn(1, "task set "+idx);
		DoAnal da=new DoAnal(0);
		da.run(tm);
		da.prn();
		
		return 0;		
	}
	
	public int test2()	{
		String tsn="adm/test1/taskset_75.txt";
		String out="adm/pi0/taskset_75.anal.0.txt";
		DoAnal da=new DoAnal(0);
		AutoAnal as=new AutoAnal("adm/test1",da);
		as.analTS(tsn,out);
		return 0;
	}

	



	public int test3() {
		DoAnal da=new DoAnal(0);
		AutoAnal as=new AutoAnal("adm/test1",da);
		as.setRS("adm/pi0");
		as.analList("a_ts_list.txt");		
		return -1;
	}
	public int test4() {
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
		Class c = z_autoanal1.class;
		z_autoanal1 m=new z_autoanal1();
		int[] aret=z_autoanal1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}