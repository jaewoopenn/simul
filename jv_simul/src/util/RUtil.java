package util;

import java.util.Random;

public class RUtil {
	private static Random g_rand=new Random();
	public double getDbl(double l, double u){
		return g_rand.nextDouble()*(u-l)+l;
	}
	
	public int getInt(int l, int u){
		return g_rand.nextInt(u-l)+l;
	}

	public double getDbl() {
		return g_rand.nextDouble();
	}

	public int getInt(int max) {
		return g_rand.nextInt(max);
	}
}
