package queue;


import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

// 이벤트 타입
enum EventType {
    ARRIVAL, DEPARTURE
}

// 이벤트 클래스: 시뮬레이션 시간 순서대로 처리할 항목
class Event implements Comparable<Event> {
    double time;
    EventType type;

    public Event(double time, EventType type) {
        this.time = time;
        this.type = type;
    }

    // PriorityQueue가 시간을 기준으로 오름차순 정렬하도록 설정
    @Override
    public int compareTo(Event other) {
        return Double.compare(this.time, other.time);
    }
}

// 고객 클래스: 도착 시간 등 정보를 저장
class Customer {
    double arrivalTime;
    double serviceTime;
    double startTime;
    double departureTime;

    public Customer(double arrivalTime, double serviceTime) {
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }
}

public class MG1 {
    // --- 설정 매개변수 ---
    private static final double ARRIVAL_RATE = 1.0; // 도착률 (lambda)
    private static final double SERVICE_TIME_MIN = 0.5; // 균일 분포 최소 서비스 시간
    private static final double SERVICE_TIME_MAX = 1.5; // 균일 분포 최대 서비스 시간
    private static final int MAX_CUSTOMERS = 10000; // 시뮬레이션할 최대 고객 수

    // --- 시뮬레이션 상태 변수 ---
    private double currentTime = 0.0;
    private int customersServed = 0;
    private boolean serverBusy = false;
    private final PriorityQueue<Event> eventQueue = new PriorityQueue<>();
    private final Queue<Customer> customerQueue = new LinkedList<>(); // 대기열
    private Customer currentCustomer = null;

    // --- 통계 변수 ---
    private double totalWaitingTime = 0.0;
    private double totalTimeInSystem = 0.0;
    private final Random random = new Random();

    // 지수 분포 난수 생성 (도착 간격 시간)
    private double getExponentialRandom(double rate) {
        return -Math.log(random.nextDouble()) / rate;
    }

    // 균일 분포 난수 생성 (서비스 시간)
    private double getUniformRandom(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    // 초기 도착 이벤트 생성 및 이벤트 큐에 추가
    private void initialize() {
        double firstArrivalTime = getExponentialRandom(ARRIVAL_RATE);
        double firstServiceTime = getUniformRandom(SERVICE_TIME_MIN, SERVICE_TIME_MAX);
        
        eventQueue.add(new Event(firstArrivalTime, EventType.ARRIVAL));
        System.out.println("M/G/1 Queue Simulation Initialized.");
    }

    // 시뮬레이션 실행 루프
    public void runSimulation() {
        initialize();

        while (customersServed < MAX_CUSTOMERS && !eventQueue.isEmpty()) {
            Event event = eventQueue.poll();
            currentTime = event.time;

            if (event.type == EventType.ARRIVAL) {
                handleArrival();
            } else if (event.type == EventType.DEPARTURE) {
                handleDeparture();
            }
        }
        
        displayResults();
    }

    // 도착 이벤트 처리
    private void handleArrival() {
        // 1. 현재 도착하는 고객 객체 생성
        double serviceTime = getUniformRandom(SERVICE_TIME_MIN, SERVICE_TIME_MAX);
        Customer newCustomer = new Customer(currentTime, serviceTime);

        // 2. 서버 상태 확인 및 처리
        if (serverBusy) {
            customerQueue.add(newCustomer); // 서버 사용 중: 대기열에 추가
        } else {
            // 서버 유휴: 즉시 서비스 시작
            startService(newCustomer);
        }

        // 3. 다음 도착 이벤트 스케줄링
        if (customersServed + customerQueue.size() < MAX_CUSTOMERS) {
            double interArrivalTime = getExponentialRandom(ARRIVAL_RATE);
            eventQueue.add(new Event(currentTime + interArrivalTime, EventType.ARRIVAL));
        }
    }

    // 서비스 시작 처리
    private void startService(Customer customer) {
        serverBusy = true;
        currentCustomer = customer;
        currentCustomer.startTime = currentTime;

        // 대기 시간 계산 및 누적
        double waitingTime = currentCustomer.startTime - currentCustomer.arrivalTime;
        totalWaitingTime += waitingTime;

        // 출발 이벤트 스케줄링
        double departureTime = currentTime + currentCustomer.serviceTime;
        eventQueue.add(new Event(departureTime, EventType.DEPARTURE));
    }

    // 출발 이벤트 처리
    private void handleDeparture() {
        // 1. 현재 고객 통계 기록
        serverBusy = false;
        customersServed++;
        currentCustomer.departureTime = currentTime;
        
        // 시스템 내 체류 시간 계산 및 누적
        double timeInSystem = currentCustomer.departureTime - currentCustomer.arrivalTime;
        totalTimeInSystem += timeInSystem;
        currentCustomer = null;

        // 2. 대기열 확인 및 다음 서비스 시작
        if (!customerQueue.isEmpty()) {
            Customer nextCustomer = customerQueue.poll();
            startService(nextCustomer);
        }
    }

    // 결과 출력
    private void displayResults() {
        double avgWaitingTime = totalWaitingTime / customersServed;
        double avgTimeInSystem = totalTimeInSystem / customersServed;
        double avgServiceTime = (SERVICE_TIME_MIN + SERVICE_TIME_MAX) / 2.0;

        // 이론적 값 (Pollaczek–Khinchine formula)
        // 로드를 계산: rho = lambda * E[S]
        double rho = ARRIVAL_RATE * avgServiceTime;
        
        // 서비스 시간 분산 (Uniform: (b-a)^2 / 12)
        double varianceServiceTime = Math.pow(SERVICE_TIME_MAX - SERVICE_TIME_MIN, 2) / 12.0;
        
        // PK 공식: E[W] = (lambda * E[S^2]) / (2 * (1 - rho))
        // E[S^2] = Var[S] + (E[S])^2
        double es2 = varianceServiceTime + Math.pow(avgServiceTime, 2);
        double pk_avgWaitingTime = (ARRIVAL_RATE * es2) / (2.0 * (1.0 - rho));
        double pk_avgTimeInSystem = pk_avgWaitingTime + avgServiceTime;

        System.out.println("\n--- M/G/1 Simulation Results ---");
        System.out.printf("Total Customers Served: %d\n", customersServed);
        System.out.printf("Final Simulation Time: %.4f\n", currentTime);
        System.out.printf("Server Utilization (rho): %.4f\n", rho);
        
        System.out.println("\n--- Performance Metrics ---");
        System.out.printf("1. Average Waiting Time (Simulation): %.4f\n", avgWaitingTime);
        System.out.printf("2. Average Time in System (Simulation): %.4f\n", avgTimeInSystem);
        
        System.out.println("\n--- Theoretical (Pollaczek-Khinchine) ---");
        System.out.printf("1. Average Waiting Time (Theory): %.4f\n", pk_avgWaitingTime);
        System.out.printf("2. Average Time in System (Theory): %.4f\n", pk_avgTimeInSystem);
    }

    public static void main(String[] args) {
        new MG1().runSimulation();
    }
}


