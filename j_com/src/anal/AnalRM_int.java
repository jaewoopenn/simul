package anal;


public class AnalRM_int extends Anal{
	public AnalRM_int() {
		super();
		g_name="RM_int";
	}	
	
	@Override
	public boolean is_sch() {
		AnalRM a=new AnalRM();
		a.init(g_tm,g_prm);
		return a.is_sch();
	}	

	@Override
	public double getExec(int p) {
		AnalRM a=new AnalRM();
		a.init(g_tm);
		double e=a.getExec(p);
		return Math.ceil(e);
	}


}
	
