package Simul;

import Basic.TaskMng;

public abstract class Anal {
	TaskMng tm;
	int g_limit=10000;
	public void init(TaskMng mng) {
		tm=mng;
	}
	public abstract void prepare();
	public abstract boolean isScheduable();
	public abstract int getResp();
	public abstract double getDropRate(double p);

}
