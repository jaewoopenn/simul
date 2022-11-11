package task;
/*

*/

import java.util.Comparator;



public class ComparatorTaskCM implements Comparator<Task>{
	public int compare(Task a, Task b) {
		if(a.isHC()==b.isHC()) {
			if(a.period< b.period)
				return -1;
			else if (a.period==b.period)
				return 0;
			else
				return 1;
		} else {
			if(a.isHC())
				return -1;
			else
				return 1;
		}
	}
}