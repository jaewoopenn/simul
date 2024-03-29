package auto;

import testPlatform.Platform5;

/*
 * util alpha
 * Figure 5: Component-MC schedulability of \textsf{FC-MCS} varying utilization bound for different alpha parameters
 */
public class auto1 {
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		System.out.println("start!");
		Platform5 p=new Platform5(); 
		p.test1();
		System.out.println("check data/fc/gra/util_alpha.csv");		
		long finish = System.currentTimeMillis();
		long timeElapsed = finish - start;		
		System.out.println(timeElapsed+"ms");		
	}
}
