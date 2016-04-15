package Simul;

import java.util.Comparator;



public class TaskComparatorLo implements Comparator<Task>{
	public int compare(Task a, Task b) {
//		int det=((double)(a.c_h)/a.period > (double)(b.c_h)/b.period) ?1:-1;
//		System.out.println(det);
		return ((double)(a.c_l)/a.period > (double)(b.c_l)/b.period) ?-1:1;
	}
}