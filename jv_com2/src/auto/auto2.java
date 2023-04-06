package auto;

import testPlatform.Platform5;

/*
 *  util prob
 *  Figure 6: Component-MC schedulability of \textsf{FC-MCS} varying the probability to be a HI-task ($P^\textsf{HI}$) for different alpha parameters
 *  
 */
public class auto2 {
	public static void main(String[] args) {
		System.out.println("start!");
		Platform5 p=new Platform5(); 
		p.test2();		
		System.out.println("check data/fc/gra/util_prob.csv");		
	}
}
