package testTask;
import basic.TaskGenMC;
import basic.TaskGenParam;
import basic.TaskMng;
import basic.TaskMngPre;
//import exp.Platform;
import utilSim.TEngine;

public class TaskGen1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=4;
	public static int total=10;
	public static int gret[]={1,1,1,0,1,1,1,0,0,0};
	public int test1()
	{
		return 0;
	}
	public int test2()
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
	public  int test3()
	{
		TaskGenParam tgp=new TaskGenParam();
		tgp.setUtil(0.5,0.8);
		tgp.setPeriod(20,50);
		tgp.setTUtil(0.001,0.1);
		TaskGenMC tg=new TaskGenMC(tgp);
		tg.generate();
		tg.prn(1);
		int tg_size=tg.size();
		TaskMngPre tm=new TaskMngPre();
		tm.setTasks(tg.getAll());
		TaskMng m=tm.freezeTasks();
//		tm.prn();
		int tm_size=m.getInfo().getSize();
		if(tg_size!=tm_size){
			System.out.println("tg:"+tg_size+", tm:"+tm_size);
			return 0;
		}
		return 1;
	}
	public  int test4()
	{
		TaskGenParam tgp=new TaskGenParam();
		tgp.setUtil(0.5,0.8);
		tgp.setPeriod(20,50);
		tgp.setTUtil(0.001,0.1);
		tgp.setRatioLH(0.25, 1);
		TaskGenMC tg=new TaskGenMC(tgp);
		tg.generate();
		tg.writeFile("test2.txt");
		return 0;
		
	}
	public  int test5()
	{
		TaskMngPre tmp=TaskMngPre.loadFile("file/test.txt");
		TaskMng m=tmp.freezeTasks();
		m.prn();
		return 1;
	}
	public  int test6()
	{
		return -1;
	}
	public  int test7()
	{
		return -1;
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
		Class c = TaskGen1.class;
		TaskGen1 m=new TaskGen1();
		int[] aret=TaskGen1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
