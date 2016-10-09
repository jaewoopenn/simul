package exp;


public abstract class AbsJob {
	public int tid;
	public int dl;
	public int exec;
	public int add_exec;
	public boolean isHI;

	public AbsJob(int tid,int dl, int exec) {
		this.tid=tid;
		this.dl = dl;
		this.exec = exec;
		this.add_exec=0;
		this.isHI=false;
	}

	public AbsJob(int tid,int dl, int exec,int add) {
		this.tid=tid;
		this.dl = dl;
		this.exec = exec;
		this.add_exec=add;
		this.isHI=true;
	}

	public abstract void prn();

}
