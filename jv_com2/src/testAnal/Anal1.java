package testAnal;

import anal.AnalEDF_AD_E;
import task.Task;
import task.TaskVec;
import util.SLog;
import util.SEngineT;

public class Anal1 {
	public static int idx=1;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	public static int log_level=2;

	public int test1() 
	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(4,2));
		tmp.add(new Task(6,1,5));
		AnalEDF_AD_E a=new AnalEDF_AD_E();
		a.init(tmp.getTM());
		double x=a.computeX();
		SLog.prn(2, ""+x);
		return -1;
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
		Class c = Anal1.class;
		Anal1 m=new Anal1();
		int[] aret=Anal1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}

}
