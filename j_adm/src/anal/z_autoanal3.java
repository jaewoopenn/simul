package anal;

import gen.SysLoad;
import task.DTaskVec;
import util.SEngineT;
import util.SLog;

public class z_autoanal3 {
	public static void init_s() {
		s_idx=1;
//		s_idx=2;
//		s_idx=3;
		
		
		s_log_level=1;
	}





	public int test1()	{
//		String tsn="adm/test1/task.txt";
		String tsn="adm/sim/taskset_75.txt";
		
		SysLoad sy=new SysLoad(tsn);
//		DoAnal da=new DoAnal(0);
		DoAnal da=new DoAnal(1);
		int sum=0;
		int i=0;
		while(true) {
			DTaskVec dt= sy.loadOne();
			if(dt==null) break;
			SLog.prn("no: "+i);
			da.run(dt);
			String s=da.getRS();
			if(s=="1")
				sum++;
			i++;
		}
		SLog.prn(2, "sum:"+sum);
		return 0;		
	}
	
	public int test2()	{
		return 0;
	}

	



	public int test3() {
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
		z_autoanal3.init_s();
		Class c = z_autoanal3.class;
		z_autoanal3 m=new z_autoanal3();
		int[] aret=z_autoanal3.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	private static int s_idx;
	private static int s_log_level;
}