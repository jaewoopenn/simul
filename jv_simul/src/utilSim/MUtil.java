package utilSim;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MUtil {
	public static double err=0.00000000000001;
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
		return String.format("%.3f", d);
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
}
