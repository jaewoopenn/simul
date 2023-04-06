package testPlatform;


import exp.PlatformCom;
import gen.ConfigGen;
import util.SLog;
import util.SEngineT;


/* 
 * DMR
 * 
 */

public class Platform4 {
//	public static int idx=1;
//	public static int idx=2;
	public static int idx=3;
//	public static int idx=4;
//	public static int idx=5;
//	public static int idx=-1;
//	public static int log_level=1;
//	public static int log_level=2;
	public static int log_level=3;
	public int kind=0;
	public int isReal=0;
//	public int prob=1;
	public int prob=4;
//	public int prob=7;

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
		p.setStart(55);
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
		if(isReal==1){
			p.setDuration(10000);
			p.setSysNum(5000);
//			p.setSysNum(1000);
		} else{
			p.setDuration(5000);
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
		p.setAlpha(0.00, 0.30);
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
//		p.isWrite=false;
		p.write_x_axis();
		p.simulCom(0);
		p.simulCom(1);
		p.simulCom(2);
		String[] lab={"EDF-VD","FC-MCS-v1","FC-MCS-v2"};
		DataAnal da=new DataAnal("fc",3);
		da.load_x("rs/util_sim_4.txt");
		for(int i=0;i<3;i++) {
			da.load_rs("rs/util_sim_4_"+i+".txt",lab[i] , i);
		}
		da.save("gra/com_dmr.csv");			
		return 1;
	}
	public  int test4() 
	{
		int set=7;
		for(int i=0;i<100;i++){
			PlatformCom p=getP();
//			SLog.prn(3, no+"");
			p.simulCom_one(0,set,i);
			p.simulCom_one(1,set,i);
		}
		return 1;
	}
	public  int test5() 
	{
		int set=7;
		int no=66;
		PlatformCom p=getP();
//		SLog.prn(3, no+"");
		p.simulCom_one(0,set,no);
		p.simulCom_one(1,set,no);
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
