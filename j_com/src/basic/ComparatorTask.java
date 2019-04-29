package basic;
/*

*/

import java.util.Comparator;



public class ComparatorTask implements Comparator<Task>{
	public int compare(Task a, Task b) {
		if(a.getUtil()> b.getUtil())
			return -1;
		else if (a.getUtil()==b.getUtil())
			return 0;
		else
			return 1;
	}
}