package task;
/*

*/

import java.util.Comparator;



public class ComparatorTaskIMC implements Comparator<Task>{
	public int compare(Task a, Task b) {
		if(a.getLIKE()> b.getLIKE())
			return -1;
		else if (a.getLIKE()==b.getLIKE())
			return 0;
		else
			return 1;
	}
}