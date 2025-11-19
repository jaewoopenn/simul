package job;



public abstract class JobMng {

	protected abstract boolean add(Job j);


	protected abstract void prn();


	protected abstract Job getCur();



	protected abstract Job removeCur();

	public boolean isIdle() {  // Real Idle
		return getCur()==null;
	}


}
