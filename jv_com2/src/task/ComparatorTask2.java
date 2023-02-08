package task;
/*

*/

import java.util.Comparator;



public class ComparatorTask2 implements Comparator<Task>{
	public int compare(Task a, Task b) {
		if(a.getLoExec()> b.getLoExec())
			return -1;
		else if (a.getLoExec()==b.getLoExec())
			return 0;
		else
			return 1;
	}
}