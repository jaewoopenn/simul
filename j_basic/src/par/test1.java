package par;

class MyRunnable implements Runnable {
    @Override
    public void run() {
    	for(int i=0;i<10;i++) {
	        System.out.println(i+"Thread running: " + Thread.currentThread().getName());
	        try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
	       
    }
}

public class test1 {

	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(new MyRunnable());
		t1.start(); // Starts a new thread
		
		Thread.sleep(500);
		Thread t2 = new Thread(new MyRunnable());
		t2.start(); // Starts a new thread
		t1.join();
		t2.join();
        System.out.println("join");
	 
	}

}
