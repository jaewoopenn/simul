package gen;
import gen.ConfigGen;
import util.SEngineT;

public class z_ConfigGen1 {
//	public static int idx=1;
//	public static int idx=2;
	public static int idx=3;
	public static int log_level=2;
	public static int gret[]={0,1,0,9,1, 1,0,0,0,0};
	public int test1() // error config
	{
		ConfigGen eg=ConfigGen.getSample();
		eg.write("com/cfg1_copy.txt");
		return 0;
	}
	public int test2() // normal config
	{
		ConfigGen eg=new ConfigGen();
		eg.load("com/cfg1_copy.txt");
		return 0;
	}
	public int test3() // print config
	{
		ConfigGen eg=new ConfigGen();
		eg.load("com/cfg1_copy.txt");
		String s=eg.readPar("util_err");
		if(s==null) 
			return 0;
		System.out.println(s);
		return 1;
	}
	public  int test4() 
	{
		return 1;
	}
	public  int test5() 
	{
		return 1;
	}
	public  int test6() 
	{
		return 1;
	}
	public  int test7()
	{
		return 1;
	}
	public  int test8()
	{
		return 0;
	}
	public  int test9()
	{
		return 0;
	}
	public  int test10()
	{
		return 0;
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = z_ConfigGen1.class;
		z_ConfigGen1 m=new z_ConfigGen1();
		int[] aret=z_ConfigGen1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}

}
