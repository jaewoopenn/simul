package auto;

import anal.DoAnal;
import gen.SysLoad;
import task.DTaskVec;
import util.SEngineT;
import util.SLog;

public class z_autoanal3 {
	public static int idx=1;
//	public static int idx=2;
//	public static int idx=3;
	public static int log_level=1;
//	public static int log_level=2;





	public int test1()	{
//		String tsn="adm/test1/task.txt";
		String tsn="adm/anal/taskset_95.txt";
		
		SysLoad sy=new SysLoad(tsn);
		sy.open();
		DoAnal da=new DoAnal(0);
		int sum=0;
		while(true) {
			DTaskVec dt= sy.loadOne2();
			if(dt==null) break;
			da.run(dt);
			String s=da.getRS();
			if(s=="1")
				sum++;
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
		Class c = z_autoanal3.class;
		z_autoanal3 m=new z_autoanal3();
		int[] aret=z_autoanal3.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}