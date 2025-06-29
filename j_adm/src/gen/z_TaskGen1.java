package gen;
import task.TaskMng;
import task.TaskSet;
import task.TaskSetUtil;
import util.MList;
import util.SEngineT;

// Simulation

public class z_TaskGen1 {
	public static int log_level=1;
	public static int idx=1;
//	public static int idx=2;
	
	public TaskGen getTG1(){
		TaskGenParam tgp=new TaskGenParam();
		tgp.setPeriod(50,300);
		tgp.setTUtil(0.02,0.3);
		tgp.setRatioLH(0.2,0.9);
		tgp.setUtil(0.90,0.99);
		tgp.setProbHI(0.5);
		return new TaskGenIMC(tgp);
	}

	public int test1()
	{
		TaskGen tg=getTG1();
		tg.genTS();
		TaskSet ts=tg.getTS();
		TaskSetUtil.writeFile("test/test.txt", ts.getArr());
		return 1;
	}
	public int test2() {
		TaskSet tmp=TaskSetUtil.loadFile(new MList("test/test.txt"));
		TaskMng tm=tmp.getTM();
		tm.prn();
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
		Class c = z_TaskGen1.class;
		z_TaskGen1 m=new z_TaskGen1();
		int[] aret=z_TaskGen1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int total=10;
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
