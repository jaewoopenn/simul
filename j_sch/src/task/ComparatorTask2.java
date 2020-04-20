package task;
/*

*/

import java.util.Comparator;



public class ComparatorTask2 implements Comparator<Task>{
	public int compare(Task a, Task b) {
		if(a.getLoUtil2()> b.getLoUtil2())
			return -1;
		else if (a.getLoUtil2()==b.getLoUtil2())
			return 0;
		else
			return 1;
	}
}