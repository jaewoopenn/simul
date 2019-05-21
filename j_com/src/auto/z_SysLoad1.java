package auto;
import com.PRM;

import anal.Anal;
import anal.AnalRM;
import task.TaskSet;
import util.SEngineT;
import util.SLog;

public class z_SysLoad1 {
	public static void init_s() {
//		int s=1;
//		int s=2;
		int s=3;
//		int s=4;
//		int s=5;
		
//		int log=1;
//		int log=2;
		int log=3;
		
		s_idx=s;
		s_log_level=log;
	}
	
	public int test1() // gen
	{
		SysLoad sy=new SysLoad("com/t1/taskset_55.txt");
		sy.open();
		TaskSet tm=sy.loadOne();
		tm.prn();
		return 1;

	}
	public int test2() // load
	{
		SysLoad sy=new SysLoad("com/test1");
		sy.open();
		TaskSet tm;
		while((tm=sy.loadOne())!=null) {
			tm.prnInfo(2);
		}
		return 0;
	}
	public int test3() // load one
	{
		String ts="com/t1/taskset_55.txt";
		SysLoad sy=new SysLoad(ts);
		sy.open();
//		int num=Integer.valueOf(ret).intValue();
		Anal a=new AnalRM();
		int p=15;
		while(true) {
			TaskSet tm=sy.loadOne();
			if(tm==null) break;
			tm.sort();

			a.init(tm);
			double e=a.getExec(p);
			String st=p+","+e;
//			PRM prm=new PRM(p,e-0.001);
			PRM prm=new PRM(p,e);
			a.setPRM(prm);
			if(a.is_sch()) {
				st+=" OK";
				
			} else {
				st+=" Not OK";
			}
			SLog.prn(3, st);
//			break;
		}


		return 0;
	}
	public  int test4() {
		String ts="com/t1/taskset_55.txt";
		SysLoad sy=new SysLoad(ts);
		sy.open();
//		int num=Integer.valueOf(ret).intValue();
		AnalRM a=new AnalRM();
		int p=10;
		TaskSet tm=sy.loadOne(4);
		tm.sort();
		tm.prnInfo(2);
		a.init(tm);
		a.prnReq(3);
		double e=a.getExec(p);
		String st=p+","+e;
		PRM prm=new PRM(p,e);
		a.setPRM(prm);
		if(a.is_sch()) {
			st+=" OK";
			
		} else {
			st+=" Not OK";
		}
		SLog.prn(3, st);
		return 1;
	}
	public  int test5() {
		String ts="com/t1/taskset_55.txt";
		SysLoad sy=new SysLoad(ts);
		sy.open();
//		int num=Integer.valueOf(ret).intValue();
		while(true) {
			TaskSet tm=sy.loadOne();
			
			if(tm==null) break;
//			tm.prn();
			tm.prnInfo(2);
		}		
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
		z_SysLoad1.init_s();
		Class c = z_SysLoad1.class;
		z_SysLoad1 m=new z_SysLoad1();
		int[] aret=z_SysLoad1.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	
	public static int s_idx;
	public static int s_log_level;
	public static int gret[]={1,0,0,1,1, 1,0,0,0,0};

}
