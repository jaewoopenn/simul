package testExp;

import basic.Task;
import basic.TaskGenMC;
import basic.TaskMngPre;
import exp.Platform;
import utilSim.TEngine;

public class Platform1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=1;
	public static int gret[]={1,1,1,0,1,1,0,0,0,0};

	public int test1()
	{
		TaskGenMC tg=new TaskGenMC();
		String fn="exp/ts2";
		tg.loadFile(fn);
//		tg.prn(1);
		TaskMngPre tm=new TaskMngPre();
		tm.setTasks(tg.getAll());
		return post(tm,20);
	}

	public int test2()
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,2,1));
		tm.add(new Task(0,3,1));
		return post(tm,7);
	}

	public int test3()
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,2,1));
		tm.add(new Task(0,5,2));
		return post(tm,10);
	}

	public  int test4()
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,2,1));
		tm.add(new Task(0,3,2));
		return post(tm,7);
	}

	public  int test5()
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,6,2));
		tm.add(new Task(0,9,6));
		return post(tm,18);
	}
	
	public  int test6()
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,2,1));
		tm.add(new Task(0,3,1));
		tm.add(new Task(0,4,1));
		return post(tm,20);
	}
	
	public  int test7()
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,10,5));
		tm.add(new Task(0,12,6));
		return post(tm,12);
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
	public int post(TaskMngPre tm,int dur){
		Platform p=new Platform();
		p.init(tm);
		return p.simul(dur);
		
	}

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = Platform1.class;
		Platform1 m=new Platform1();
		int[] aret=Platform1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
