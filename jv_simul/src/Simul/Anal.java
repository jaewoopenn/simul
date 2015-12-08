package Simul;

public abstract class Anal {
	TaskMng tm;
	
	public void init(TaskMng mng) {
		tm=mng;
	}
	public abstract void prepare();
	public abstract boolean isScheduable();


}
