package job;



public abstract class JobSys {
	protected JobSimul g_js;
	protected int g_id=0;
	protected int g_t=0;
	protected int g_val=0;
	protected int g_dem_base=-1;
	public boolean add(int dl, int e) {
        if(g_dem_base==-1) {
        	g_dem_base=g_t;
        }
        return add_in(dl,e);
	}
	protected abstract boolean add_in(int dl, int e) ;
	public abstract void prn_detail();
	public abstract void prn_ok();
	public void exec(int len) {
		int et=g_t+len;
		while(g_t<et) {
			int rs=g_js.simul_one(g_t);
			if(rs==0) {
				reset();
			}
			g_t++;
		}
	}
	protected abstract void reset();
	public int getVal() {
		return g_val;
	}
	public void init() {
		g_id=0;
		g_t=0;
		g_val=0;
		g_dem_base=-1;
		init_in();
	}
	public abstract void init_in();

	
}
