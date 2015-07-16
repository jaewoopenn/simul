package Test;

public class Mock {
	int ret[]={0,0,3};
	public static int test1()
	{
//		System.out.println("hihi");
		return 0;
	}
	public static int test2()
	{
//		System.out.println("hihi");
		return 0;
	}
	public static int test3()
	{
//		System.out.println("hihi");
		return 0;
	}
	public void run()
	{
		if(test1()!=ret[0]) System.out.println("Error 1");
		if(test2()!=ret[1]) System.out.println("Error 2");
		if(test3()!=ret[2]) System.out.println("Error 3");
		System.out.println("End");
	}
	public static void main(String[] args) {
		Mock mock=new Mock();
		mock.run();
	}

}
