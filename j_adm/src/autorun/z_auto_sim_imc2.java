package autorun;


import util.SEngineT;
import util.SLog;

// MC-RUN
// a_imc/dmr_imc.py

public class z_auto_sim_imc2 {
	private static int s_idx;
	private static int s_log_level;
	private String g_path;
	private double g_p_ms;
	private int g_dur;
	private String g_ts;
	
	
	public static void init_s() {
		int s=1;
		
		s_idx=s;
		
		s_log_level=2;
	}
	

	public void init_sim() {
		g_path="adm/test1";
		g_dur=5000;
		g_p_ms=0.1;
	}
	public int test1() 
	{
		init_sim();
		g_path="adm/test1";
		g_ts="a_ts_list.txt";
		String rs_path="adm/scn";
		g_p_ms=0.2;
		Platform_IMC p=new Platform_IMC(g_path,rs_path);

		
		p.setP_MS(g_p_ms);
		SLog.prn(2, "p:"+g_p_ms);
		p.setDur(g_dur);
		
		p.gen_scn(g_ts,7,10,10,10);
		return 0;
	}
	public int test2() 
	{
		init_sim();
		g_path="adm/test1";
		g_ts="a_ts_list.txt";
		String rs_path="adm/scn";
		g_p_ms=0.2;
		Platform_IMC p=new Platform_IMC(g_path,rs_path);

		
		p.setP_MS(g_p_ms);
		SLog.prn(2, "p:"+g_p_ms);
		p.setDur(g_dur);
		
		p.run_scn(g_ts,1,7,9,9,10);
		return 0;
	}
	public int test3() 
	{
		return 0;
	}
	public  int test4() 
	{
		return 0;		
	}
	public  int test5() 
	{
		return 0;
	}
	public  int test6() 
	{
		return 0;
	}
	public  int test7()
	{
		return 0;
	}
	public  int test8() {
		return 0;
	}
	public  int test9() {
		return 0;
	}
	public  int test10() {
		return 0;
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		z_auto_sim_imc2.init_s();
		Class c = z_auto_sim_imc2.class;
		z_auto_sim_imc2 m=new z_auto_sim_imc2();
		int[] aret=z_auto_sim_imc2.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
