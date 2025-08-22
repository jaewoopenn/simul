package task;
/*

*/

import java.util.Comparator;



public class ComparatorTask implements Comparator<Task>{
	public int compare(Task a, Task b) {
		if(a.getLoUtil()> b.getLoUtil())
			return -1;
		else if (a.getLoUtil()==b.getLoUtil())
			return 0;
		else
			return 1;
	}
}