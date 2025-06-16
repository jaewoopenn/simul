package task;
/*

Individual Task 

*/



public class TaskMC extends Task{
	

	public TaskMC(int period, int c_l) {
		super(period,c_l,0,false);
	}

	public TaskMC(int period, int c_l, int c_h) {
		super(period,c_l,c_h,true);
	}


}

