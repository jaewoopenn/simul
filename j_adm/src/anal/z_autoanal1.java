package anal;

import gen.SysLoad;
import task.DTaskVec;
import util.SEngineT;
import util.SLog;

public class z_autoanal1 {
	public static void init_s() {
		s_idx=1;
//		s_idx=2;
//		s_idx=3;
		
		
		s_log_level=1;
	}


	public int test1()	{
		int idx=0;
		String ts="adm/test2/taskset_59.txt";
		SysLoad sy=new SysLoad(ts);
		sy.open();
		sy.moveto(idx);
		
		DTaskVec tm=sy.loadOne();
		SLog.prn(1, "task set "+idx);
		DoAnal da=new DoAnal(3);
		da.run(tm);
		
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
		z_autoanal1.init_s();
		Class c = z_autoanal1.class;
		z_autoanal1 m=new z_autoanal1();
		int[] aret=z_autoanal1.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	private static int s_idx;
	private static int s_log_level;
}