package TestThesis;
import Basic.Comp;
import Basic.CompGen;
import Basic.CompGenParam;
import Basic.TaskGen;
import Basic.TaskGenMC;
import Basic.TaskMng;
import Exp.Platform;
import Util.Log;
import Util.TEngine;

public class CompGen1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=2;
	
	public static int gret[]={1,1,1,0,1,1,1,0,0,0};
	public int test1()
	{
		CompGenParam p=new CompGenParam();
		p.set_tu(0.02,0.3);
		p.set_ht_lt(0.0,1.0);
		p.set_ratio(1, 4);
		CompGen tg=new CompGen(p);
		int max_num=2;
		for(int i=0;i<max_num;i++){
			Comp c=tg.genComp(i);
			c.prn(1);
		}
		return 1;
	}
	public int test2()
	{
		CompGenParam p=new CompGenParam();
		p.set_tu(0.02,0.3);
		p.set_ht_lt(0.0,1.0);
		p.set_ratio(1, 4);
		p.set_util(0.80, 0.85);
		CompGen tg=new CompGen(p);
		tg.generate();
		tg.prn(1);
		return tg.check();
	}
	public  int test3()
	{
		TaskGenMC tg=new TaskGenMC();
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
		TaskGenMC tg=new TaskGenMC();
		tg.setUtil(0.5,0.8);
		tg.setPeriod(20,50);
		tg.setTUtil(0.001,0.1);
		tg.generate();
		tg.writeFile("test2.txt");
		return 0;
		
	}
	public  int test5()
	{
		TaskGenMC tg=new TaskGenMC();
		tg.loadFile("test2.txt");
		tg.prn(1);
		int tg_size=tg.size();
		TaskMng tm=new TaskMng();
		tm.setTasks(tg.getAll());
		tm.freezeTasks();
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
		TaskGenMC tg=new TaskGenMC();
		tg.loadFile("test2.txt");
		TaskMng tm=new TaskMng();
		tm.setTasks(tg.getAll());
		Platform p=new Platform();
		p.init(tm);
		return p.simul(20);
	}
	public  int test7()
	{
		TaskGenMC tg=new TaskGenMC();
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
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = CompGen1.class;
		CompGen1 m=new CompGen1();
		int[] aret=CompGen1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
