package anal;

import com.PRM;

import sample.TS1;
import task.TaskMng;
import util.SEngineT;
import util.SLog;

public class z_Anal1 {
	public static int idx=1;
//	public static int idx=2;
	public static int log_level=1;

	public int test1() 
	{
		TaskMng tm=TS1.tm1();
		Anal a=new AnalRM();
//		PRM p=new PRM(3,1.5);
		PRM p=new PRM(3,1.667);
		a.init(tm,p);
		if(a.is_sch())
			SLog.prn(1, "OK");
		else
			SLog.prn(1, "Not OK");
		return -1;
	}

	public int test2() {
		return 0;
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
		Class c = z_Anal1.class;
		z_Anal1 m=new z_Anal1();
		int[] aret=z_Anal1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
