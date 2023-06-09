package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class MCal {
	public static double err=0.0000000000001;
	public static int btoi(boolean b){
		if(b) return 1;
		return 0;
	}
	public static int combi(int n,int r){
		return facto(n)/(facto(r)*facto(n-r));
	}
	public static int facto(int n){
		int rs=1;
		for (int i=2;i<=n;i++){
			rs*=i;
		}
		return rs;
	}
	public static String getStr(double d){
		return String.format("%.4f", d);
	}
	public static void sendMail(String msg) {
		try{
		    String[] cmdArray = {"C:/Python27/python.exe", 
		    		"c:/my/py/mail.py",msg};    
		    Process p =Runtime.getRuntime().exec(cmdArray);
		    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		    String line = null;
		   
		    while((line = br.readLine()) != null){
		        System.out.println(line);
		    }		
		}catch(Exception e){
		    System.out.println(e);
		}
		
	}
	public static long lcm(long a, long b) {
		int gcd_value = gcd((int)a, (int)b);
		if (gcd_value == 0) return 0; // 인수가 둘다 0일 때의 에러 처리
		return Math.abs( (a * b) / gcd_value );
	}

	public static int gcd(int a, int b) {
		while (b != 0) {
			int temp = a % b;
			a = b;
			b = temp;
		}
		return Math.abs(a);
	}
	public static long lcm(int[] nums) {
		long c=1;
		for(int num:nums) {
			c=lcm(c,num);
		}
		return c;
	}

}
