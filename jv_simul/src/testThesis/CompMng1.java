package testThesis;

import comp.OldCompMng;
import comp.OldComp;
import utilSim.TEngine;

public class CompMng1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=1;
	
	public static int gret[]={0,1,1,1,0,1,0,0,0,0};



	public int test1() 
	{
//		CompMng cm=new CompMng();
//		TaskMng tm=new TaskMng();
//		tm.add(new Task(0,8,1));
//		tm.add(new Task(0,12,1,5));
//		tm.freezeTasks();
//		cm.addComp(tm);
//		tm=new TaskMng();
//		tm.add(new Task(0,8,1));
//		tm.add(new Task(0,12,1,5));
//		tm.freezeTasks();
//		cm.addComp(tm);
//		cm.prn(2);
		return -1;
	}

	public int test2() 
	{
		OldCompMng cm=new OldCompMng();
		OldComp c=new OldComp(0,1.0/8,1.0/12,5.0/12);
		cm.addComp(c);
		c=new OldComp(0,1.0/8,1.0/12,5.0/12);
		cm.addComp(c);
		cm.prn2();
		return -1;
	}

	public int test3() 
	{
		OldComp c=new OldComp(0,1.0/8,1.0/12,5.0/12);
		c.prn(1);
		return -1;
	}
	
	public  int test4() 
	{
		return -1;
	}
	
	public  int test5() 
	{
		return -1;
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
		return -1;
	}
	
	public  int test9()
	{
		return -1;
	}
	public  int test10()
	{
		return -1;
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = CompMng1.class;
		CompMng1 m=new CompMng1();
		int[] aret=CompMng1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
