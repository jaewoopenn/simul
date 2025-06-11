package anal;

import com.PRM;

public class AnalRM_int extends Anal{
	public AnalRM_int() {
		super();
		g_name="CSF(ceil)";
	}	
	
	@Override
	public boolean is_sch() {
		AnalRM a=new AnalRM();
		a.init(g_ts,g_prm);
		return a.is_sch();
	}	

	@Override
	public double getExec(int p) {
		AnalRM a=new AnalRM();
		a.init(g_ts);
		double e=a.getExec(p);
		return Math.ceil(e);
	}

	@Override
	public boolean checkSch(PRM p) {
		// TODO !! Auto-generated method stub
		return false;
	}


}
	
