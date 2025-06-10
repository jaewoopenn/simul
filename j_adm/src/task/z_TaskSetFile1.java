package task;
import util.MList;
import util.SEngineT;
import util.SLog;

public class z_TaskSetFile1 {
//	public static int idx=1;
	public static int idx=2;
//	public static int idx=3;
//	public static int idx=4;
	public static int log_level=1;
	public int test1()
	{
		MList fu=new MList();
		TaskSetUtil.initStage(fu, 5);
		TaskSetUtil.writeTask(fu, new Task(3,1));
		TaskSetUtil.writeTask(fu, new Task(4,1));
		TaskSetUtil.writeTask(fu, new Task(5,1));
		TaskSetUtil.nextStage(fu);
		TaskSetUtil.writeTask(fu, new Task(5,1,4));
		TaskSetUtil.nextStage(fu);
		TaskSetUtil.remove(fu, 3);
		TaskSetUtil.remove(fu, 2);
		TaskSetUtil.writeTask(fu, new Task(5,1,3));
		fu.saveTo("test/test.txt");
		return 1;
	}
	public int test2()
	{
		DTaskVec dt=TaskSetUtil.loadFile2(new MList("test/test.txt"));
		int num=dt.getNum();
		for(int i=0;i<num;i++) {
			TaskSet tmp=new TaskSet(dt.getVec(i));
			TaskMng tm=tmp.getTM();
			tm.prn();
			SLog.prn(2, "---");
		}
		return 0;
	}
	
	public int test3() {
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(3,1));
		tmp.add(new Task(4,1));
		TaskMng tm=tmp.getTM();
		tm.prn();
		TaskSetUtil.writeFile("test/test.txt",tm.getTasks());
		return 1;
	}
	public  int test4() {
		TaskSet tmp=TaskSetUtil.loadFile(new MList("test/test.txt"));
		TaskMng tm=tmp.getTM();
		tm.prn();
		return 0;
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
		Class c = z_TaskSetFile1.class;
		z_TaskSetFile1 m=new z_TaskSetFile1();
		int[] aret=z_TaskSetFile1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}