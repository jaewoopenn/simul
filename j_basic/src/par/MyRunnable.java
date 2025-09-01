package par;

import util.MRand;
import util.SLog;

public class MyRunnable implements Runnable {
	protected MRand g_rutil=new MRand();
	public int ret=0;
    @Override
    public void run() {
    	for(int i=0;i<10;i++) {
    		ret=g_rutil.getInt(100);
    		SLog.prn(1,"Thread running: " + Thread.currentThread().getName()+","+i+"-th rs =  "+ret);
	        try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    }
	public int getRet() {
		return ret;
	}

}
