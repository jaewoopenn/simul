/* 
 * Static Class
 */

package util;

public class SLog {
	private static int g_lv=9;
	public static void set_lv(int lv){
		g_lv=lv;
	}

	public static void prn(int lv,Object s)
	{
		if(g_lv<=lv)
			System.out.println(s);
	}

	public static void prnc(int lv,Object s) {
		if(g_lv<=lv)
			System.out.print(s);
	}
	
	public static void form(int lv,String s,int v) {
		if(g_lv<=lv)
			System.out.format(s,v);
	}
	
	public static void err(String s){
		System.out.println("ERR:"+s);
		System.exit(1);
	}
	public static void err_if(boolean b, String s) {
		if(b) {
			err(s);
		}
	}
	public static void prnDblc(int lv,double f){
		if(g_lv<=lv)
			System.out.printf("%.3f", f);		
	}
}
