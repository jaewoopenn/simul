package task;

import task.Task;
import task.TaskSet;
import task.TaskSetUtil;
import util.MList;
import util.SEngineT;

public class z_TaskSetEx1 {
	public static int idx=1;
	public static int log_level=1;
	public int test1()
	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(3,1));
		tmp.add(new Task(4,1));
		TaskVec tme=new TaskVec(tmp.getVec());
		TaskSet tm=new TaskSet(tme);
		tm.prn();
		TaskSetUtil.writeFile("test/test.txt",tm.getArr());
		return 1;
	}
	public int test2()
	{
		TaskVec tmp=TaskSetUtil.loadFile(new MList("test/test.txt"));
		TaskSet tm=new TaskSet(tmp);
		tm.prn();
		return 0;
	}
	public int test3() {
		return 0;
	}
	public  int test4() {
		return 1;
	}
	public  int test5() {
		return 1;
	}
	public  int test6() {
		return 1;
	}
	public  int test7() {
		return 1;
	}
	public  int test8() {
		return 0;
	}
	public  int test9() {
		return 0;
	}
	public  int test10() {
		return 0;
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = z_TaskSetEx1.class;
		z_TaskSetEx1 m=new z_TaskSetEx1();
		int[] aret=z_TaskSetEx1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};

}
