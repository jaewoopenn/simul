package task;

/*

*/

import java.util.Comparator;



public class ComparatorHighTask implements Comparator<Task>{
	public int compare(Task a, Task b) {
//		int det=((double)(a.c_h)/a.period > (double)(b.c_h)/b.period) ?1:-1;
//		System.out.println(det);
//		return det;
		if(a.getHiUtil()> b.getHiUtil())
			return -1;
		else if (a.getHiUtil()==b.getHiUtil())
			return 0;
		else
			return 1;
	}
}