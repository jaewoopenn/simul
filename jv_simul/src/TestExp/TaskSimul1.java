package TestExp;

import Util.TEngine;
import Basic.TaskMng;
import Exp.JobMng;
import Exp.JobSimul;
import Exp.TaskSimul;

public class TaskSimul1 {
//	public static int idx=-1;
	public static int idx=1;
	public static int gret[]={2,0,-1,-1,-1, -1,-1,-1,-1,-1};

	public TaskMng ts1()	{
		TaskMng tm=new TaskMng();
		tm.addTask(2,1);
		tm.addTask(3,1);
		tm.freezeTasks();
		return tm;
	}
	

	public int test1()	{
		TaskSimul ts=new TaskSimul(ts1());
		ts.init();
		return -1;
	}
	public int test2() {
		return -1;
	}
	
	public  int test3()	{
		return -1;
	}
	
	public  int test4()	{
		return -1;
	}
	
	public  int test5() {
		return -1;
	}
	public  int test6()	{
		return -1;
	}
	public  int test7()	{
		return -1;
	}
	public  int test8()	{
		return -1;
	}
	public  int test9() {
		return -1;
	}
	public  int test10() {
		return -1;
	}

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = TaskSimul1.class;
		TaskSimul1 m=new TaskSimul1();
		int[] aret=TaskSimul1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,1);
	}

}
