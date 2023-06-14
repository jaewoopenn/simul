package anal;

import com.PRM;

import task.Task;
import util.MCal;
import util.SLog;

public class AnalEDF_int extends Anal{
	public AnalEDF_int() {
		super();
		g_name="EDF_int";
	}	
	
	
	@Override
	public boolean is_sch() {
		Anal a=new AnalEDF();
		a.init(g_ts,g_prm);
		return a.is_sch();
	}	

	@Override
	public double getExec(int p) {
		Anal a=new AnalEDF();
		a.init(g_ts);
		double e=a.getExec(p);
		return Math.ceil(e);
	}

	@Override
	public boolean checkSch(PRM p) {
		// TODO Auto-generated method stub
		return false;
	}}
	
