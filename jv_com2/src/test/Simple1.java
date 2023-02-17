package test;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import util.SLog;
import util.MUtil;
import util.SEngineT;

public class Simple1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=5;
	public static int total=10;
	public static int gret[]={1,1,0,0,0, 0,0,0,0,0};
	public int test1() // error config
	{
		int ret=MUtil.facto(5);
		SLog.prn(1, "rs:"+ret);
		return 1;
	}
	public int test2() 
	{
		int ret=MUtil.combi(5, 3);
		SLog.prn(1, "rs:"+ret);
		ret=MUtil.combi(5, 2);
		SLog.prn(1, "rs:"+ret);
		ret=MUtil.combi(4, 2);
		SLog.prn(1, "rs:"+ret);
		ret=MUtil.combi(6, 2);
		SLog.prn(1, "rs:"+ret);
		ret=MUtil.combi(6, 4);
		SLog.prn(1, "rs:"+ret);
		return 1;
	}
	public int test3() 
	{
		double d=0.612312512;
		String str=String.format("%.3f", d);
		SLog.prn(1, "hihi "+str);
		return 0;
	}
	public  int test4() 
	{
		try{
		    String[] cmdArray = {"C:/Python27/python.exe", 
		    		"c:/my/py/mail.py","Alarm 111"};    
//		    String[] cmdArray = {"C:/Python27/python.exe", 
//		    		"c:/my/py/test.py"};    
		    Process p =Runtime.getRuntime().exec(cmdArray);
		    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		    String line = null;
		   
		    while((line = br.readLine()) != null){
		        System.out.println(line);
		    }		
		}catch(Exception e){
		    System.out.println(e);
		}
		return 0;
	}
	public  int test5() 
	{
		MUtil.sendMail("Complete");
		return 0;
	}
	public  int test6() 
	{
		return 0;
	}
	public  int test7()
	{
		return 0;
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
		Class c = Simple1.class;
		Simple1 m=new Simple1();
		int[] aret=Simple1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}

}
