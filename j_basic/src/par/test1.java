package par;

import java.util.stream.IntStream;

import util.MRand;
import util.SLog;

class MyRunnable implements Runnable {
	protected MRand g_rutil=new MRand();
	public int ret=0;
    @Override
    public void run() {
    	for(int i=0;i<10;i++) {
    		ret=g_rutil.getInt(100);
    		SLog.prn(1,i+" "+ret+" Thread running: " + Thread.currentThread().getName());
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

public class test1 {

	public static void main(String[] args) throws InterruptedException {
		SLog.set_lv(1);
		int n=10;
		MyRunnable[] mr=new MyRunnable[n];
		Thread[] th=new Thread[n];
		int[] ran=IntStream.range(0,n).toArray();
		for(int i:ran) {
			mr[i]=new MyRunnable();
			th[i] = new Thread(mr[i]);
			th[i].start(); // Starts a new thread
			Thread.sleep(1000/n);
		}
		
		for(int i:ran) {
			th[i].join();
		}
		
		int ret=0;
		for(int i:ran) {
			ret+=mr[i].getRet();
		}
        SLog.prn(1,"join "+ret);
	 
	}

}
