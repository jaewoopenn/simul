package Test;
import Simul.Platform;
import Simul.Task;
import Simul.TaskGen;
import Simul.TaskMng;
import Test.TEngine;

public class TaskGen1 {
//	public static int idx=-1;
	public static int idx=7;
	public static int total=1;
	public static int gret[]={1,1,1,0,1,1,1,0,0,0};
	public int test1()
	{
		TaskGen tg=new TaskGen();
		tg.setPeriod(20,50);
		tg.setTUtil(0.02,0.3);
		for(int i=0;i<1000;i++){
			Task t=tg.genTask(0);
			if (!tg.chkTask(t)) return 0;
		}
		return 1;
	}
	public int test2()
	{
		TaskGen tg=new TaskGen();
		tg.setUtil(0.8);
		tg.setPeriod(20,50);
		tg.setTUtil(0.001,0.1);
		tg.generate();
		tg.prn();
		return tg.check();
	}
	public  int test3()
	{
		TaskGen tg=new TaskGen();
		tg.setUtil(0.8);
		tg.setPeriod(20,50);
		tg.setTUtil(0.001,0.1);
		tg.generate();
		tg.prn();
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
		tg.setUtil(0.8);
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
//		tg.prn();
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
		Class c = TaskGen1.class;
		TaskGen1 m=new TaskGen1();
		int[] aret=TaskGen1.gret;
		int sz=TaskGen1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,1);
	}

}
