package gen;
import task.TaskMng;
import task.TaskSet;
import task.TaskSetUtil;
import util.SEngineT;
import util.SLog;

// Simulation

public class z_TaskGen2 {
	public static int log_level=1;
//	public static int idx=1;
	public static int idx=2;
	
	public TaskGen getTG1(){
		TaskGenParam tgp=new TaskGenParam();
		tgp.setPeriod(50,300);
		tgp.setTUtil(0.02,0.3);
		tgp.setRatioLH(0.2,0.9);
		tgp.setUtil(0.90,0.99);
		tgp.setProbHI(0.5);
		TaskGenIMC tg=new TaskGenIMC(tgp);
		return tg;
	}
	public TaskGen getTG2(){
		TaskGenParam tgp=new TaskGenParam();
		tgp.setPeriod(50,300);
		tgp.setTUtil(0.02,0.3);
		tgp.setRatioLH(0.2,0.9);
		tgp.setUtil(0.90,0.99);
		tgp.setProbHI(0.5);
		TaskGenMC tg=new TaskGenMC(tgp);
		return tg;
	}
	public int test1()
	{
		TaskGen tg=getTG1();
//		TaskGen tg=getTG2();
		tg.generate();
		TaskSet ts=tg.getTS();
		TaskSetUtil.writeFile("test/test.txt", ts.getArr());
		return 1;
	}
	public int test2() {
		TaskGen tg=getTG1();
//		TaskGen tg=getTG2();
		tg.generate();
		TaskSet ts=tg.getTS();
		TaskMng tm=ts.getTM();
		tm.prn();
		tm.prnInfo();
		if(tg.isOK())
			SLog.prn(1, "OK");
		else
			SLog.prn(1, "not OK");
			
		return 1;
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
		Class c = z_TaskGen2.class;
		z_TaskGen2 m=new z_TaskGen2();
		int[] aret=z_TaskGen2.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int total=10;
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
