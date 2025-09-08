package sim;

import gen.SysLoad;
import task.DTaskVec;
import util.SEngineT;

// 테스트는 여기에서. 아직 많이 구현 부

@SuppressWarnings("unused")
public class z_autosimul1 {
	public static void init_s() {
//		s_idx=1;
		s_idx=2;
//		s_idx=3;
		
		
		s_log_level=1;
//		s_log_level=2;
	}
	
	public int test1()	{
		String tsn="adm/sim/taskset_75.txt";
		int n=10;

		String out="adm/test.txt";
		SysLoad sy=new SysLoad(tsn);
		sy.open();
		sy.moveto(n);
		DTaskVec dt=sy.loadOne();
		DoSimul ds=new DoSimul(0);
		ds.setProb(0.3);
		ds.setDur(2000);
		ds.run(dt);
		ds.prn();
		return 0;
	}
	public int test2() {
		String tsn="adm/test1/taskset_75.txt";
		String out="adm/pi0/taskset_75.sim.0.txt";
		DoSimul ds=new DoSimul(0);
		ds.setProb(0.3);
		ds.setDur(1000);
		AutoSimul as=new AutoSimul("adm/test1",ds);
		as.simulTS(tsn,out);
		return 0;
	}
	

	public int test3() {
		DoSimul ds=new DoSimul(0);
		ds.setProb(0.3);
		ds.setDur(1000);
		AutoSimul as=new AutoSimul("adm/test1",ds);
		as.setRS("adm/pi0");
		as.simulList("a_ts_list.txt");
		return 0;
	}
	public  int test4() {
		for(int i=0;i<2;i++) {
			DoSimul ds=new DoSimul(i);
			ds.setProb(0.3);
			ds.setDur(1000);
			AutoSimul as=new AutoSimul("adm/test1",ds);
			as.setRS("adm/pi0");
			as.simulList("a_ts_list.txt");
		}
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
		z_autosimul1.init_s();
		Class c = z_autosimul1.class;
		z_autosimul1 m=new z_autosimul1();
		int[] aret=z_autosimul1.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	private static int s_idx;
	private static int s_log_level;
}