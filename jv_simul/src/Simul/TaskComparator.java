package Simul;

import java.util.Comparator;



public class TaskComparator implements Comparator{
	public int compare(Object arg0, Object arg1) {
		Task a= (Task)arg0;
		Task b=(Task)arg1;
		int det=((double)(a.c_h)/a.period > (double)(b.c_h)/b.period) ?1:0;
		System.out.println(det);
		return det;
	}
}