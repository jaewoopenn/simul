package job;


public class Density implements Comparable<Density>{
	public Job j;
	public double den;

	public Density(Job j,double den) {
		this.j=j;
		this.den=den;
	}
	
	@Override
	public int compareTo(Density o) {
		double o_d = o.den;  
		if (den>o_d)
			return 1;
		else if (den==o_d)
			return 0;
		else
			return -1;
	}


}
