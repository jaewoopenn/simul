package oldComp;

import java.util.Comparator;



public class CompComparator implements Comparator<OComp>{
	public int compare(OComp a, OComp b) {
//		System.out.println(a.getCompUtil());
//		int det=(a.getCompUtil() > b.getCompUtil()) ?1:-1;
//		System.out.println(det);
		return (a.getCompUtil()> b.getCompUtil()) ?-1:1;
	}
}