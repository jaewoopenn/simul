package TestTask;
import Basic.Task;
import Basic.TaskGen;
import Basic.TaskGenMC;
import Basic.TaskMng;
import Basic.TaskMngPre;
import Simul.Analysis;
import Util.Log;
import Util.TEngine;

public class TaskGenMC1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=6;
	public static int total=10;
	public static int gret[]={1,1,1,1,1,1,-1,-1,-1,-1};
	public TaskGen getTG1(){
		TaskGenMC tg=new TaskGenMC();
		tg.setPeriod(50,300);
		tg.setTUtil(0.02,0.3);
		tg.setRatioLH(0.2,0.9);
		tg.setUtil(0.90,0.99);
		tg.setProbHI(0.5);
		return tg;
	}
	public TaskGen getTG2(){
		TaskGenMC tg=new TaskGenMC();
		tg.setPeriod(50,300);
		tg.setTUtil(0.02,0.3);
		tg.setRatioLH(0.7,0.9);
		tg.setUtil(0.95,1.00);
		tg.setProbHI(0.5);
		return tg;
	}

	public int test1()
	{
		TaskGen tg=getTG1();
		for(int i=0;i<5;i++){
			Task t=tg.genTask(i);
			Log.prn(1, "tid:"+t.tid+", p:"+t.period+", l:"+t.c_l+", h:"+t.c_h+", Xi:"+t.is_HI);
		}
		return 1;
	}
	public int test2()
	{
		TaskGen tg=getTG1();
		tg.generate();
		tg.prn(1);
		return tg.check();

	}
	public  int test3()
	{
		TaskGen tg=getTG2();
		tg.generate();
		TaskMngPre tm=new TaskMngPre();
		tm.setTasks(tg.getAll());
		TaskMng m=tm.freezeTasks();
		return Analysis.anal_EDF_VD(m);
	}
	public  int test4()
	{
		TaskGen tg=getTG2();
		int id=0;
		while(true){
			tg.generate();
			double u=tg.getUtil();
			TaskMngPre tm=new TaskMngPre();
			tm.setTasks(tg.getAll());
			TaskMng m=tm.freezeTasks();
			if(Analysis.anal_EDF_VD(m)==1) 
				Log.prn(1, "id:"+id+" util:"+u+" Y");
			else
				Log.prn(1, "id:"+id+" util:"+u+" N");
			id++;
			if (id==10) break;
		}
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
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = TaskGenMC1.class;
		TaskGenMC1 m=new TaskGenMC1();
		int[] aret=TaskGenMC1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
