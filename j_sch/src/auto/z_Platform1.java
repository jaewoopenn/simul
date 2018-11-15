package auto;


import anal.AnalEDF;
import basic.TaskMng;
import gen.ConfigGen;
import gen.SysGen;
import gen.SysGenTM;
import gen.SysLoad;
import util.FUtil;
import util.Log;
import util.TEngine;

public class z_Platform1 {
	public static int idx=2;
	public static int log_level=1;

	public int test1() 
	{
		String path="test/t1/";
		FUtil fu=new FUtil(path+"a_cfg_list.txt");
		FUtil fu_ts=new FUtil(path+"a_ts_list.txt");
		FUtil fu_rs=new FUtil(path+"a_x_list.txt");
		int n=fu.load();
//		Log.prn(1, n+" ");
		for(int i=0;i<fu.size();i++) {
			ConfigGen cfg=new ConfigGen(fu.get(i));
			cfg.readFile();
			SysGen eg=new SysGenTM(cfg);
			String fn=eg.get_fn();
			eg.gen(fn);
			fu_ts.write(fn);
			String mod=eg.get_mod();
			fu_rs.write(mod);
		}
		fu_ts.save();
		fu_rs.save();
		
		return -1;
	}

	public int test2() 
	{
		String path="test/t1/";
		String cl="a_cfg_list.txt";
		Platform p=new Platform(path);
		p.gen(cl);
		return -1;
	}
	
	public int test3() 
	{
		SysLoad sy=new SysLoad("test/t1/taskset_55");
		sy.open();
		while(true) {
			TaskMng tm=sy.loadOne();
			if(tm==null) break;
			tm.prnInfo();
		}
		return -1;		
	}
	
	public  int test4() 
	{
		String path="test/t1/";
		FUtil fu=new FUtil(path+"a_ts_list.txt");
//		FUtil fu_rs=new FUtil(path+"a_rs_list.txt");
		fu.load();
		for(int i=0;i<fu.size();i++) {
			String fn=fu.get(i);
			int n=0;
			SysLoad sy=new SysLoad(fn);
			sy.open();
			while(true) {
				TaskMng tm=sy.loadOne();
				if(tm==null) break;
				n++;
			}
			Log.prn(1, fn+" "+n);
		}
//		fu_rs.save();
		return -1;
		
	}
	
	public  int test5() 
	{
		String path="test/t1/";
		String ts="a_ts_list.txt";
		Platform p=new Platform(path);
		p.anal(ts,0);
		p.anal(ts,1);
		return -1;
	}
	public  int test6() 
	{
		String path="test/t1/";
		String ts="taskset_55";
		String out=ts+".rs";
		Platform p=new Platform(path);
		p.anal_one(ts,out,new AnalEDF());
		return -1;
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
		Class c = z_Platform1.class;
		z_Platform1 m=new z_Platform1();
		int[] aret=z_Platform1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
