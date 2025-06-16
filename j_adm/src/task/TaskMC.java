package task;
/*

Individual Task 

*/


import util.SLog;
import util.MCal;
import util.SLogF;

public class TaskMC extends Task{
	

	public TaskMC(int period, int c_l) {
		super(period,c_l,0,false);
	}

	public TaskMC(int period, int c_l, int c_h) {
		super(period,c_l,c_h,true);
	}


}

