package testExp;


import anal.ConfigGen;
import exp.Platform;
import utilSim.Log;
import utilSim.TEngine;


// schedulability 
public class Platform3 {
	public static int idx=2;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	public static int log_level=1;
	public Platform getP(){
		return getP1();
//		return getP2();
	}
	
	public Platform getP1() {
		Platform p=getCommmon();
		p.setTSName("util");
		p.setKinds(0);
		p.setStart(55);
		p.setSize(10);
//		p.setStart(100);
//		p.setSize(1);
		p.setStep(5);
		return p;
	}
	public Platform getP2() {
		Platform p=getCommmon();
		p.setTSName("prob_hi");
		p.setKinds(1);
		p.setStart(5);
		p.setSize(19);
		p.setStep(5);
		return p;
	}

	public Platform getCommmon(){
		Platform p=new Platform();
		p.setPath("com");
		p.setCfg_fn("cfg/cfg");
		p.setAlpha(0,0.3);
//		p.setSysNum(5000);
//		p.setSysNum(1000);
		p.setSysNum(100);
//		p.setSysNum(10);
//		p.setSysNum(1);
		return p;
		
	}
	
	public int test1() 
	{
		ConfigGen eg=ConfigGen.getCfg();
		eg.setParam("p_lb","50");
		eg.setParam("p_ub","300");
		eg.setParam("c_lb","0.1");
//		eg.setParam("c_lb","0.05");
		eg.setParam("c_ub","0.2");
		eg.setParam("u_lb", "0.85");
		eg.setParam("u_ub", "0.90");
		eg.setParam("tu_lb","0.02");
		eg.setParam("tu_ub","0.1");
		Platform p=getP();
		p.writeComCfg(eg);
//		for(int i=0;i<4;i++){
//			p.setAlpha(i*0.25,i*0.25+0.25);
//			p.writeComCfg(eg);
//		}	
		Log.prn(3, "cfg");
		return 1;
	}
	public int test2() 
	{
		Platform p=getP();
		p.genCom();
//		for(int i=0;i<4;i++){
//			p.setAlpha(i*0.25,i*0.25+0.25);
//			p.genCom();
//		}	
		return 1;
	}
	public int test3() 
	{
		Platform p=getP();
//		p.isWrite=false;
		p.setRS("0");
		p.write_x_axis();
		p.setAlpha(0,0);
		p.setRS("0");
		p.analCom();
		double step=0.25;
		for(int i=1;i<5;i++){
			p.setAlpha((i-1)*step,i*step);
			p.setRS(i+"");
			p.analCom();
		}	
//		MUtil.sendMail("ICG anal OK");
		return 1;
	}
	public  int test4() 
	{
		Platform p=getP();
		p.prnCom();
		return 1;
	}
	public  int test5() 
	{
		int set=9;
		Platform p=getP();
		int ret1,ret2;
		for(int i=0;i<100;i++){
			p.setAlpha(0,0.25);
			ret1=p.analCom(set,i);
			p.setAlpha(0.75,1.00);
			ret2=p.analCom(set,i);
			if(ret1==0&&ret2==1)
				break;
//			if(ret1==1&&ret2==0)
//				break;
		}
		return 0;
	}
	public  int test6() 
	{
		int set=3;
		Platform p=getP();
		int ret1;
		p.setAlpha(0,0);
		for(int i=0;i<100;i++){
			ret1=p.analCom(set,i);
			Log.prn(2,ret1);
			if(ret1==0)
				break;
		}
		return 0;
	}
	public  int test7()
	{
		int set=2;
		int i=73;
		Platform p=getP();
		int ret1;
		p.setAlpha(0,0);
		ret1=p.analCom(set,i);
		Log.prn(2,ret1);
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
		Class c = Platform3.class;
		Platform3 m=new Platform3();
		int[] aret=Platform3.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
