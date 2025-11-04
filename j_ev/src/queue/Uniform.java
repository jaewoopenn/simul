package queue;

import util.MRand;

public class Uniform {
	private double min;
	private double max;
	private MRand mr;
	public Uniform(double min,double max) {
		this.min=min;
		this.max=max;
		mr=MRand.init();
	}
	public double next() {
		return getUniformRandom(min,max);
	}
	// 예시: 균일 분포 (Uniform Distribution) for Service Time (G)
	public double getUniformRandom(double min, double max) {
	    return min + (max - min) * mr.getDbl();
	}
}
