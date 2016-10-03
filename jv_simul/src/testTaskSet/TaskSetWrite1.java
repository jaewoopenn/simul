package testTaskSet;
import basic.TaskGen;
import basic.TaskGenMC;
import utilSim.TEngine;

public class TaskSetWrite1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=2;
	public static int gret[]={1,1,1,0,1,1,1,0,0,0};

	public int test1() //
	{
		TaskGen tg=new TaskGen();
		tg.setUtil(0.5,0.8);
		tg.setPeriod(20,50);
		tg.setTUtil(0.001,0.1);
		tg.generate();
		tg.prn(1);
		return tg.check();
	}
	public int test2() // Write
	{
		TaskGen tg=new TaskGen();
		tg.setUtil(0.45,0.5);
		tg.setPeriod(20,50);
		tg.setTUtil(0.01,0.1);
		tg.generate();
		tg.writeFile("exp/ts/test1.txt");
		return 1;
	}
	public  int test3() // MC task 
	{
		TaskGenMC tg=new TaskGenMC();
		tg.setRatioLH(0.25, 1);
		tg.setUtil(0.45,0.5);
		tg.setPeriod(300,500);
		tg.setTUtil(0.01,0.1);
		tg.setProbHI(0.5);
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
