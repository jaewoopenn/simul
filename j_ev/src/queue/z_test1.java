package queue;




import java.util.Random;

import util.MCal;
import util.MRand;
import util.SEngineT;
import util.SLog;

public class z_test1 {
	public static void init_s() {
//		s_idx=1;
//		s_idx=2;
		s_idx=3;
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
        // M/M/1 파라미터 설정
        double lambda = 10.0; // 시간당 10명 도착
        double mu = 12.0;    // 시간당 12명 처리
        int numCustomers = 100; // 시뮬레이션할 고객 수

        Random random = new Random();

        System.out.println("M/M/1 시뮬레이션 시작...");
        System.out.println("평균 도착률 (λ): " + lambda);
        System.out.println("평균 서비스율 (μ): " + mu);
        System.out.println("----------------------------------------");

        double totalInterArrivalTime = 0.0;
        double totalServiceTime = 0.0;

        for (int i = 0; i < numCustomers; i++) {
            // 지수 분포를 따르는 도착 간격 시간 계산
            // -ln(1 - U) / λ 공식 사용 (U는 [0, 1] 사이의 균일 분포 난수)
            double interArrivalTime = -Math.log(1 - random.nextDouble()) / lambda;
            totalInterArrivalTime += interArrivalTime;

            // 지수 분포를 따르는 서비스 시간 계산
            // -ln(1 - U) / μ 공식 사용
            double serviceTime = -Math.log(1 - random.nextDouble()) / mu;
            totalServiceTime += serviceTime;

            System.out.printf("고객 %d: 도착 간격 = %.4f 시간, 서비스 시간 = %.4f 시간%n", i + 1, interArrivalTime, serviceTime);
        }

        double averageInterArrivalTime = totalInterArrivalTime / numCustomers;
        double averageServiceTime = totalServiceTime / numCustomers;

        System.out.println("----------------------------------------");
        System.out.printf("총 %d명의 고객에 대한 시뮬레이션 결과:%n", numCustomers);
        System.out.printf("평균 도착 간격: %.4f 시간 (이론값: %.4f)%n", averageInterArrivalTime, 1 / lambda);
        System.out.printf("평균 서비스 시간: %.4f 시간 (이론값: %.4f)%n", averageServiceTime, 1 / mu);
		return 1;
	}
	public int test3() {
		int p=10;
		int occur=3; //p 시간당 occur 발
		double l = (double)occur/p;
		Poisson po=new Poisson(l);
		p=10;
		occur=2; //p 시간당 occur 처리
		l = (double)occur/p;
		Poisson po2=new Poisson(l);
		double next=0;
		for(int i=0;i<10;i++) {
			next+=po.next();
			double exec=po2.next();
			double dl=po2.next()+5;
            SLog.prn(MCal.getStr(next)+"\t\t"+MCal.getStr(exec)+"\t\t"+MCal.getStr(next+dl));
			
		}
		return 1;
	}
	public  int test4() {
		return -1;
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
