package TestThesis;
import Simul.Analysis;
import Simul.Task;
import Simul.TaskGen;
import Simul.TaskMng;
import Util.Log;
import Util.TEngine;

public class TaskGen1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=1;
	public static int total=10;
	public static int gret[]={1,1,1,1,1,1,-1,-1,-1,-1};
	public TaskGen getTG1(){
		TaskGen tg=new TaskGen();
		tg.setPeriod(50,300);
		tg.setTUtil(0.02,0.3);
		tg.setRatioLH(0.2,0.9);
		tg.setUtil(0.80,0.90);
		tg.setProbHI(0.5);
		return tg;
	}
	public TaskGen getTG2(){
		TaskGen tg=new TaskGen();
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
		tg.generate();
		tg.prn(1);
		return tg.check();
	}
	public int test2()
	{
		return 1;
	}
	public  int test3()
	{
		TaskGen tg=getTG2();
		tg.generate();
		TaskMng tm=new TaskMng();
		tm.setTasks(tg.getAll());
		tm.freezeTasks();
		return Analysis.anal_EDF_VD(tm);
	}
	public  int test4()
	{
		TaskGen tg=getTG2();
		int id=0;
		while(true){
			tg.generate();
			double u=tg.getMCUtil();
			TaskMng tm=new TaskMng();
			tm.setTasks(tg.getAll());
			tm.freezeTasks();
			if(Analysis.anal_EDF_VD(tm)==1) 
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
		Class c = TaskGen1.class;
		TaskGen1 m=new TaskGen1();
		int[] aret=TaskGen1.gret;
		int sz=TaskGen1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
