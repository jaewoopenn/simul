package sim;


public class SysMng {
	private double g_prob;
	private double g_x;
	public void setMS_Prob(double p) {
		g_prob=p;
		
	}

	public double getMS_Prob() {
		return g_prob;
	}

	public void setX(double d) {
		g_x=d;
	}
	public double getX() {
		return g_x;
	}
	
}
