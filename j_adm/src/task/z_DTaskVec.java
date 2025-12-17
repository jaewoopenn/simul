package task;
import sim.DTUtil;
import util.SEngineT;
import util.SLog;

public class z_DTaskVec {
//	public static int idx=1;
//	public static int idx=2;
	public static int idx=3;
//	public static int idx=4;
	public static int log_level=1;
	public int test1()
	{
		DTaskVec tasks=new DTaskVec(2);
		tasks.add( new Task(5,1,2,false));
		tasks.add( new Task(3,1,2,false));
		tasks.addTasks(0);
		DTUtil.copy(tasks,0,1);
		tasks.remove(1,1);
		DTUtil.prn(tasks);
		
		
		return 1;
	}
	// 지금 추가한걸.... reject 시켜야 한다. 
	public int test2()
	{
		DTaskVec tasks=new DTaskVec(3);
		tasks.add( new Task(5,1,2,false));
		tasks.add( new Task(3,1,2,false));
		tasks.addTasks(0);
		DTUtil.copy(tasks,0,1);
		tasks.add( new Task(12,2,3,false));
		tasks.addTasks(1);
//		tasks.reject();
		SLog.prn("rejected ");
		DTUtil.copy(tasks,1,2);
		tasks.remove(2,0);
		DTUtil.prn(tasks);
		return 0;
	}
	
	public int test3() {
		DTaskVec tasks=new DTaskVec(3);
		tasks.add( new Task(5,1,2,false));
		tasks.add( new Task(3,1,2,false));
		tasks.addTasks(0);
		DTUtil.copy(tasks,0,1);
		tasks.add( new Task(12,2,3,false));
		tasks.addTasks(1);
		DTUtil.copy(tasks,1,2);
		tasks.remove(2,0);
		
		TaskMng tm=DTUtil.getCurTM(tasks);
		TaskUtil.prn(tm);
		SLog.prn("----");
		
		tasks.nextStage();
		tasks.reject();
		SLog.prn("rejected ");
		tm=DTUtil.getCurTM(tasks);
		TaskUtil.prn(tm);
		SLog.prn("----");

		tasks.nextStage();
		tm=DTUtil.getCurTM(tasks);
		TaskUtil.prn(tm);
		
		
		return 1;
	}
	public  int test4() {
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
		Class c = z_DTaskVec.class;
		z_DTaskVec m=new z_DTaskVec();
		int[] aret=z_DTaskVec.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}