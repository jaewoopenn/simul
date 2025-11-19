package gen;

import util.MRand;

public class JPoisson {
	private double lambda;
	private MRand mr;
	public JPoisson(double l) {
		lambda=l;
		mr=MRand.init();
	}
	public double next() {
		return genExp(lambda);
	}
    private  double genExp(double rate) {
        return -Math.log(1 - mr.getDbl()) / rate;
    }

}
