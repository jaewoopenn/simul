package Test;
import Simul.Platform;
import Simul.Task;
import Simul.TaskGen;
import Simul.TaskMng;
import Util.Log;
import Util.TEngine;

public class TaskGenMC1 {
//	public static int idx=-1;
	public static int idx=2;
	public static int total=10;
	public static int gret[]={1,1,1,0,1,1,1,0,0,0};
	public int test1()
	{
		TaskGen tg=new TaskGen();
		tg.setPeriod(50,300);
		tg.setTUtil(0.02,0.5);
		tg.setRatioLH(0.2,0.9);
		for(int i=0;i<10;i++){
			Task t=tg.genMCTask(i);
			Log.prn(1, "tid:"+t.tid+", p:"+t.period+", l:"+t.c_l+", h:"+t.c_h);
		}
		return 1;
	}
	public int test2()
	{
		TaskGen tg=new TaskGen();
		tg.setPeriod(50,300);
		tg.setTUtil(0.02,0.5);
		tg.setRatioLH(0.2,0.9);
		Task t=tg.genMCTask(0);
		Log.prn(1, "tid:"+t.tid+", p:"+t.period+", l:"+t.c_l+", h:"+t.c_h);
		t=tg.genTask(1);
		Log.prn(1, "tid:"+t.tid+", p:"+t.period+", l:"+t.c_l+", h:"+t.c_h);
		
		return 1;

	}
	public  int test3()
	{
		TaskGen tg=new TaskGen();
		tg.setUtil(0.5,0.8);
		tg.setPeriod(20,50);
		tg.setTUtil(0.001,0.1);
		tg.generate();
		tg.prn(1);
		int tg_size=tg.size();
		TaskMng tm=new TaskMng();
		tm.setTasks(tg.getAll());
//		tm.prn();
		int tm_size=tm.size();
		if(tg_size!=tm_size){
			System.out.println("tg:"+tg_size+", tm:"+tm_size);
			return 0;
		}
		return 1;
	}
	public  int test4()
	{
		TaskGen tg=new TaskGen();
		tg.setUtil(0.5,0.8);
		tg.setPeriod(20,50);
		tg.setTUtil(0.001,0.1);
		tg.generate();
		tg.writeFile("test2.txt");
		return 0;
		
	}
	public  int test5()
	{
		TaskGen tg=new TaskGen();
		tg.loadFile("test2.txt");
		tg.prn(1);
		int tg_size=tg.size();
		TaskMng tm=new TaskMng();
		tm.setTasks(tg.getAll());
		tm.prn();
		int tm_size=tm.size();
		if(tg_size!=tm_size){
			System.out.println("tg:"+tg_size+", tm:"+tm_size);
			return 0;
		}
		return 1;
	}
	public  int test6()
	{
		TaskGen tg=new TaskGen();
		tg.loadFile("test2.txt");
		TaskMng tm=new TaskMng();
		tm.setTasks(tg.getAll());
		Platform p=new Platform();
		p.init(tm);
		return p.simul(20);
	}
	public  int test7()
	{
		TaskGen tg=new TaskGen();
		tg.loadFile("t1/taskset2");
		TaskMng tm=new TaskMng();
		tm.setTasks(tg.getAll());
		Platform p=new Platform();
		p.init(tm);
		return p.simul(20);
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
		Class c = TaskGenMC1.class;
		TaskGenMC1 m=new TaskGenMC1();
		int[] aret=TaskGenMC1.gret;
		int sz=TaskGenMC1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,1);
	}

}
