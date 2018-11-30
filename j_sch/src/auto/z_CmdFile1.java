package auto;
import util.TEngine;

public class z_CmdFile1 {
	public static int idx=3;
	public static int log_level=1;
	public static int gret[]={0,1,0,9,1, 1,0,0,0,0};
	public int test1() // error config
	{
		CmdFile eg=getCmd();
		eg.write("cmd/test.txt");
		return 0;
	}
	public int test2() // normal config
	{
		CmdFile eg=new CmdFile("cmd/test.txt");
		eg.readFile();
		String s=eg.readPar("auto");
		System.out.println(s);
		return 0;
	}
	public int test3() // print config
	{
		CmdFile eg=new CmdFile("cmd/test.txt");
		eg.readFile();
		eg.prn();
		return 1;
	}
	public  int test4() // get config
	{
		return 1;
	}
	public  int test5() // write config
	{
		return 1;
	}
	public  int test6() // test config
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
	public CmdFile getCmd()	{
		CmdFile eg=new CmdFile();
		eg.setParam("auto","1");
		eg.setParam("wad","qwe");
		return eg;

	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = z_CmdFile1.class;
		z_CmdFile1 m=new z_CmdFile1();
		int[] aret=z_CmdFile1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
