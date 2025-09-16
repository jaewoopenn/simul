package job;

public class Dem implements Comparable<Dem>{
	public int t;
	public int d;
	@Override
	public int compareTo(Dem o) {
		double o_t = o.t;  
		if (t>o_t)
			return 1;
		else if (t==o_t)
			return 0;
		else
			return -1;
	}

}
