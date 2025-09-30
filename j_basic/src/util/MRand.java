package util;

import java.util.Random;

public class MRand {
	private Random g_rand;
	public MRand() {
		g_rand=new Random();
	}
	public double getDbl(double l, double u){
		return g_rand.nextDouble()*(u-l)+l;
	}
	
	public int getInt(int l, int u){
		return g_rand.nextInt(u-l+1)+l;
	}

	public double getDbl() {
		return g_rand.nextDouble();
	}

	public int getInt(int max) {
		return g_rand.nextInt(max+1);
	}
	public boolean getBool() {
		return g_rand.nextBoolean();
	}
	public static MRand init() {
		return new MRand();
	}
	public static void example() {
		MRand mr=MRand.init();
		int i=mr.getInt(10);
		SLog.prn("n:"+i);
		
	}
}
