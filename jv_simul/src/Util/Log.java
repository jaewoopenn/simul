package Util;

public class Log {
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
}