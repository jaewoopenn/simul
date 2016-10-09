package testComp;


import basic.Task;
import basic.TaskMng;
import basic.TaskMngPre;
import comp.Comp;
import comp.CompMng;
import utilSim.TEngine;

public class Comp1 {
	public static int idx=1;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	public static int log_level=1;
	public int test1() 
	{
		CompMng cm=new CompMng();
		Comp c=new Comp();
		TaskMngPre tmp=new TaskMngPre();
		tmp.add(new Task(0,6,1));
		tmp.add(new Task(0,8,1));
		TaskMng tm=tmp.freezeTasks();
		c.setTM(tm);
		cm.addComp(c);
		
		c=new Comp();
		tmp=new TaskMngPre();
		tmp.add(new Task(0,7,1));
		tmp.add(new Task(0,9,1));
		tm=tmp.freezeTasks();
		c.setTM(tm);
		cm.addComp(c);
		
		return 0;
	}
	public int test2() 
	{
		return 0;
	}
	public int test3() 
	{
		return 0;
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
		Class c = Comp1.class;
		Comp1 m=new Comp1();
		int[] aret=Comp1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
