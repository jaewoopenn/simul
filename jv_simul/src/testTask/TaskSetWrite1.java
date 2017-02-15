package testTask;
import basic.TaskGenMC;
import basic.TaskGenParam;
import util.TEngine;

public class TaskSetWrite1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=2;
	public static int gret[]={1,1,1,0,1,1,1,0,0,0};

	public int test1() //
	{
		TaskGenParam tgp=new TaskGenParam();
		tgp.setUtil(0.5,0.8);
		tgp.setPeriod(20,50);
		tgp.setTUtil(0.001,0.1);
		TaskGenMC tg=new TaskGenMC(tgp);
		tg.generate();
		tg.prn(1);
		return tg.check();
	}
	public int test2() // Write
	{
		TaskGenParam tgp=new TaskGenParam();
		tgp.setUtil(0.45,0.5);
		tgp.setPeriod(20,50);
		tgp.setTUtil(0.01,0.1);
		TaskGenMC tg=new TaskGenMC(tgp);
		tg.generate();
		tg.writeFile("exp/ts/test1.txt");
		return 1;
	}
	public  int test3() // MC task 
	{
		TaskGenParam tgp=new TaskGenParam();
		tgp.setRatioLH(0.25, 1);
		tgp.setUtil(0.45,0.5);
		tgp.setPeriod(300,500);
		tgp.setTUtil(0.01,0.1);
		tgp.setProbHI(0.5);
		TaskGenMC tg=new TaskGenMC(tgp);
		tg.generate();
		tg.prn(1);
//		tg.writeFile("test/ts/test_mc.txt");
		return 1;
	}
	public  int test4()
	{
		return 0;
	}
	public  int test5()
	{
		return 0;
	}
	public  int test6()
	{
		return 0;
	}
	public  int test7()
	{
		return 0;
	}
	public  int test8()
	{
		return 0;
	}
	public  int test9()
	{
		return 0;
	}
	public  int test10()
	{
		return 0;
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = TaskSetWrite1.class;
		TaskSetWrite1 m=new TaskSetWrite1();
		int[] aret=TaskSetWrite1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
