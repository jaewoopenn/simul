package job;



public abstract class JobSys {
	protected JobSimul g_js;
	protected int g_id=0;
	protected int g_t=0;
	protected int g_val=0;
	protected int g_dem_base=-1;
	public abstract boolean add(int dl, int e, int o, double v) ;
	public abstract void prn_dbf();
	public abstract void prn_ok();
	public abstract void exec(int i);
	
}
