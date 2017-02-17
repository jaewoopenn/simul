import util.MUtil;

public class test2 {

	public static void main(String[] args) {
		for(int i:MUtil.getLoop(3)){
			System.out.println("hh"+i);
		}
		for(int i:MUtil.getLoop(5,7)){
			System.out.println("hh"+i);
		}
	}

}
