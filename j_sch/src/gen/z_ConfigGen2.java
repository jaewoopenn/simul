package gen;
import gen.ConfigGen;
import util.MList;
import util.SLog;
import util.SEngineT;

public class z_ConfigGen2 {
	public static int idx=1;
	public static int log_level=1;
	public int test1() // error config
	{
		ConfigGen eg=ConfigGen.getPredefined();
		String path="test/t1";
		MList fu=new MList();
		eg.setParam("subfix", path);
		eg.setParam("num","10");
		int base=50;
		for(int i=0;i<10;i++){
			int lb=i*5+base;
			SLog.prn(2, lb+"");
			eg.setParam("u_lb", (lb)*1.0/100+"");
			eg.setParam("u_ub", (lb+5)*1.0/100+"");
			eg.setParam("mod", (lb+5)+"");
			String fn=path+"/cfg_"+i+".txt";
			eg.setFile(fn);
			eg.write();
			fu.add(fn);
		}
		fu.save(path+"/a_cfg_list.txt");
		return 1;
	}
	public int test2() {
		return -1;
	}
	public int test3() {
		return 0;
	}
	public  int test4() {
		return 1;
	}
	public  int test5() {
		return 1;
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
		Class c = z_ConfigGen2.class;
		z_ConfigGen2 m=new z_ConfigGen2();
		int[] aret=z_ConfigGen2.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
