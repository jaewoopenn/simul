package task;
/*

*/



public class TaskSeq {
	public static int tid=0;

	public static int getID() {
		tid++;
		return tid;
	}
	public static void reset() {
		tid=0;
	}
}

