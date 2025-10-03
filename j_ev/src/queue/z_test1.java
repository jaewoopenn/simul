package queue;




import util.MCal;
import util.MRand;
import util.SEngineT;
import util.SLog;

public class z_test1 {
	public static void init_s() {
		s_idx=1;
//		s_idx=2;
//		s_idx=3;
//		s_idx=4;
//		s_idx=5;
		
		
		s_log_level=1;
	}

    // Method to generate exponential random variables
    private  double genExp(double rate, MRand mr) {
        return -Math.log(1 - mr.getDbl()) / rate;
    }
	public int test1() 
	{
		MRand mr=MRand.init();
//        double lambda = 2.0; // Arrival rate
        double lambda = 1.5; // Arrival rate
        double mu = 3.0;     // Service rate
//        double mu = 3.0;     // Service rate
        int numCustomers = 10; // Number of customers to simulate
        double next = genExp(lambda, mr);
        SLog.prn(MCal.getStr(next)+"");
        double add=0;
        for (int i = 0; i < numCustomers; i++) {
            add =  genExp(lambda, mr);
            next+=add;
            // Generate next arrival time
            double serviceTime = genExp(mu, mr);
            SLog.prn(MCal.getStr(next)+"\t"+MCal.getStr(add)+"\t"+MCal.getStr(serviceTime));
        }
		return -1;
	}

	public int test2() {
		return -1;
	}
	public int test3() {
		return -1;
	}
	public  int test4() {
		return 1;
	}
	public  int test5() {
		return -1;
	}
	public  int test6() {
		return 1;
	}
	public  int test7() {
		return 1;
	}
	public  int test8() {
		return 0;
	}
	public  int test9() {
		return 0;
	}
	public  int test10() {
		return 0;
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		z_test1.init_s();
		Class c = z_test1.class;
		z_test1 m=new z_test1();
		int[] aret=z_test1.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	private static int s_idx;
	private static int s_log_level;

}
