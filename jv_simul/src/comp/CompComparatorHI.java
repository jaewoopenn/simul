package comp;

import java.util.Comparator;



public class CompComparatorHI implements Comparator<OldComp>{
	public int compare(OldComp a, OldComp b) {
//		System.out.println(a.getCompUtil());
//		int det=(a.getCompUtil() > b.getCompUtil()) ?1:-1;
//		System.out.println(det);
		return (a.get_ht_hu()> b.get_ht_hu()) ?-1:1;
	}
}