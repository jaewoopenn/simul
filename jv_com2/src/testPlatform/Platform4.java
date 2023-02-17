package testPlatform;


import exp.PlatformCom;
import gen.ConfigGen;
import util.SLog;
import util.SEngineT;


// schedulability 
public class Platform4 {
//	public static int idx=1;
	public static int idx=2;
//	public static int idx=3;
//	public static int idx=4;
//	public static int idx=5;
//	public static int idx=-1;
//	public static int log_level=1;
//	public static int log_level=2;
	public static int log_level=3;
	public int kind=0;
	public int isReal=0;
//	public int prob=1;
//	public int prob=4;
	public int prob=7;

	public PlatformCom getP(){
		if(kind==0)
			return getP1();
		else
			return getP2();
	}
	
	
	public PlatformCom getP1() {
		PlatformCom p=getCommmon();
		p.setTSName("util_sim");
		p.setKinds(0);
		p.setStart(50);
		p.setSize(10);
//		p.setStart(100);
//		p.setSize(1);
		p.setStep(5);
		return p;
	}
	public PlatformCom getP2() {
		PlatformCom p=getCommmon();
		p.setTSName("prob_sim");
		p.setKinds(1);
		p.setStart(5);
		p.setSize(19);
		p.setStep(5);
		return p;
	}

	public PlatformCom getCommmon(){
		PlatformCom p=new PlatformCom();
		p.setPath("fc");
		p.setCfg_fn("cfg/cfg");
		p.setAlpha(0.0,0.25);
		if(isReal==1){
			p.setDuration(10000);
			p.setSysNum(5000);
//			p.setSysNum(1000);
		} else{
			p.setDuration(1000);
			p.setSysNum(100);
		}
		p.setProb(prob*0.1);
		p.setRS(prob+"");

		return p;
		
	}
	
	public int test1() 
	{
		ConfigGen eg=ConfigGen.getCfg();
		eg.setParam("p_lb","50");
		eg.setParam("p_ub","300");
		eg.setParam("c_lb","0.1");
		eg.setParam("c_lb","0.05");
//		eg.setParam("c_ub","0.2");
		eg.setParam("u_lb", "0.85");
		eg.setParam("u_ub", "0.90");
		eg.setParam("tu_lb","0.02");
		eg.setParam("tu_ub","0.1");
		PlatformCom p=getP();
		p.writeComCfg(eg);
		SLog.prn(3, "cfg");
		return 1;
	}
	public int test2() 
	{
		PlatformCom p=getP();
		p.genCom(true);
		return 1;
	}
	public int test3() 
	{
		PlatformCom p=getP();
//		p.setAlpha(0.25,0.75);
//		p.isWrite=false;
		p.write_x_axis();
		p.simulCom(0);
		p.simulCom(1);
		return 1;
	}
	public  int test4() 
	{
		int set=0;
		int no=72;
		for(int i=0;i<100;i++){
			no=i;
			PlatformCom p=getP();
			p.setAlpha(0.1,0.3);
			SLog.prn(3, no+"");
//			p.simulCom_one(0,set,no);
			p.simulCom_one(1,set,no);
		}
		return 1;
	}
	public  int test5() 
	{
		int set=8;
		int no=24;
		PlatformCom p=getP();
		p.setAlpha(0.25,0.75);
		SLog.prn(3, no+"");
		p.simulCom_one(0,set,no);
		return 1;
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
		Class c = Platform4.class;
		Platform4 m=new Platform4();
		int[] aret=Platform4.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}