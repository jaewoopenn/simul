package testMP;

import part.Partition;
import anal.AnalEDF;
import anal.AnalEDF_VD;
import basic.TaskMng;
import sysEx.TS_MP1;
import util.Log;
import util.TEngine;

public class Part1 {
//	public static int idx=-1;
	public static int idx=3;
	public static int log_level=2;


	public int test1()	{
		TaskMng tm=TS_MP1.ts2();
		Partition p=new Partition(new AnalEDF_VD(),tm.getTaskSet());
		p.anal();
		return 0;
	}
	public int test2() {
		TaskMng tm=TS_MP1.ts3();
		Partition p=new Partition(new AnalEDF_VD(),tm.getTaskSet());
		p.anal();
		
		p.prn();
//		TaskSet ts=p.getTS(0);
//		ts.prn();
		return 0;
	}
	
	public  int test3()	{
		TaskMng tm=TS_MP1.ts3();
		Partition p=new Partition(new AnalEDF(),tm.getTaskSet());
//		Partition p=new Partition(new AnalEDF_VD(),tm.getTaskSet());
		p.anal();
		Log.prn(2,""+p.size());
		
		return 0;
		
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
		Class c = Part1.class;
		Part1 m=new Part1();
		int[] aret=Part1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
