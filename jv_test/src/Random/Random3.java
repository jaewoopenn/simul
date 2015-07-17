package Random;

import java.util.Random;


public class Random3 {
	public static void main(String[] args)  {
		Random random = new Random();
		for(int i=0;i<10;i++){
	        double r = random.nextDouble()*0.8+0.2;
			System.out.println(r);
		}
	}

}
