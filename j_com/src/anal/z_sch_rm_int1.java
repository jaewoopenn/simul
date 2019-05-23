package anal;



import com.PRM;

import sample.TS1;
import task.TaskSet;
import util.SLog;
import util.SEngineT;

public class z_sch_rm_int1 {
	public static int idx=2;
	public static int log_level=3;

	
	public int test1()
	{
		TaskSet tm=TS1.tm2();
		int p=3;
		Anal a=new AnalRM();
		a.init(tm);
		double exec=a.getExec(p);
		String st="exec:"+exec;
		a=new AnalRM_int();
		a.init(tm);
		exec=a.getExec(p);
		st+=" exec:"+exec;
		SLog.prn(3,st );
		return 0;
	}
	public int test2() 
	{
		TaskSet tm=TS1.tm2();
		int p=3;
		Anal a=new AnalRM();
		a.init(tm);
		double exec=a.getExec(p);
		PRM prm=new PRM(p,exec);
		a.init(tm,prm);
		String st="exec:"+exec+a.is_sch();
		a=new AnalRM_int();
		a.init(tm);
		exec=a.getExec(p);
		prm=new PRM(p,exec);
		a.init(tm,prm);
		st+=" exec:"+exec+a.is_sch();
		SLog.prn(3,st );
		return 0;
	}
	public  int test3()
	{
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
		Class c = z_sch_rm_int1.class;
		z_sch_rm_int1 m=new z_sch_rm_int1();
		int[] aret=z_sch_rm_int1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={0,0,0,0,0,0,0,0,0,0};

}
