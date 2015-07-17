package Random;

import java.util.Random;


public class Random2 {
	public static void main(String[] args)  {
		Random random = new Random();
		for(int i=0;i<10;i++){
	        int r = random.nextInt(10);
			System.out.print(r+" ");
		}
	}

}
