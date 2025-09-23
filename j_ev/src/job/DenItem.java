package job;


public class DenItem implements Comparable<DenItem>{
	public Job j;
	public double den;

	public DenItem(Job j,double den) {
		this.j=j;
		this.den=den;
	}
	
	@Override
	public int compareTo(DenItem o) {
		double o_d = o.den;  
		if (den>o_d)
			return 1;
		else if (den==o_d)
			return 0;
		else
			return -1;
	}


}
