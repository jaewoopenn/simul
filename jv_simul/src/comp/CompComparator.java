package comp;

import java.util.Comparator;



public class CompComparator implements Comparator<OldComp>{
	public int compare(OldComp a, OldComp b) {
//		System.out.println(a.getCompUtil());
//		int det=(a.getCompUtil() > b.getCompUtil()) ?1:-1;
//		System.out.println(det);
		return (a.getCompUtil()> b.getCompUtil()) ?-1:1;
	}
}