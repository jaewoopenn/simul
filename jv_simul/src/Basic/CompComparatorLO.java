package Basic;

import java.util.Comparator;



public class CompComparatorLO implements Comparator<Comp>{
	public int compare(Comp a, Comp b) {
//		System.out.println(a.getCompUtil());
//		int det=(a.getCompUtil() > b.getCompUtil()) ?1:-1;
//		System.out.println(det);
		return (a.get_lu()> b.get_lu()) ?-1:1;
	}
}