package testMP;
//move 


import basic.TaskMng;
import util.TEngine;

public class CoreMng3 {
	public static int idx=1;
	public static int log_level=2;


	public int test1()	{
		String fn="mp/ts/prob_sim_170/taskset_52";
		TaskMng tm=TaskMng.getFile(fn);
		tm.prnInfo();
		return 0;
	}
	
	public int test2() {
		return 0;
	}
	
	public  int test3()	{
		return 0;
	}
	
	public  int test4()	{
		return 0;
	}
	
	public  int test5() {
		return 0;
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
		Class c = CoreMng3.class;
		CoreMng3 m=new CoreMng3();
		int[] aret=CoreMng3.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
