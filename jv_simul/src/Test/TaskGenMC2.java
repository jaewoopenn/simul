package Test;
import Simul.Analysis;
import Simul.Task;
import Simul.TaskGen;
import Simul.TaskMng;
import Util.Log;
import Util.TEngine;

// Simulation

public class TaskGenMC2 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=1;
	public static int total=10;
	public static int gret[]={1,1,1,1,1,1,-1,-1,-1,-1};
	public TaskGen getTG1(){
		TaskGen tg=new TaskGen();
		tg.setFlagMC(true);
		tg.setPeriod(50,300);
		tg.setTUtil(0.02,0.3);
		tg.setRatioLH(0.2,0.9);
		tg.setUtil(0.90,0.99);
		tg.setProbHI(0.5);
		return tg;
	}

	public int test1()
	{
		TaskGen tg=getTG1();
		tg.generate();
		tg.writeFile("test2.txt");
		return 1;
	}
	public int test2()
	{
		return 1;

	}
	public  int test3()
	{
		return 1;
	}
	public  int test4()
	{
		return 1;
		
	}
	public  int test5()
	{
		return 1;
	}
	public  int test6()
	{
		return 1;
	}
	public  int test7()
	{
		return 1;
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
	public static void main(String[] args) throws Exception {
		Class c = TaskGenMC2.class;
		TaskGenMC2 m=new TaskGenMC2();
		int[] aret=TaskGenMC2.gret;
		int sz=TaskGenMC2.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
