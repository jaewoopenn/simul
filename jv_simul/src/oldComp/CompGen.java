package oldComp;

import java.util.Vector;

import utilSim.Log;

public class CompGen {
	private CompGenParam g_param;
	private Vector<OComp> g_comps;
	
	public CompGen(CompGenParam param) {
		g_param=param;
	}

	public void generate() {
		while(true){
			g_comps=new Vector<OComp>();
			genSys();
			if(check()==1) break;
		}
	}
	private void genSys()
	{
		int tid=0;
		OComp t;
		while(getMCUtil()<=g_param.get_util_u()){
			t=genComp(tid);
			g_comps.addElement(t);
			tid++;
		}
		g_comps.remove(g_comps.size()-1);
	}
	

	public OComp genComp(int tid) {
		OComp tsk=g_param.genComp(tid);
		return tsk;
	}
	


	public int check(){
		return g_param.check(getMCUtil());
	}
	
	// getting
	public void prn() {
		for(OComp c:g_comps) {
			c.prn();
		}
		Log.prn(1, "MC util:"+getMCUtil());
			
	}


	
	public double getMCUtil(){
		double loutil=0;
		double hiutil=0;
		for(OComp c:g_comps){
			loutil+=c.get_lt_lu();
			loutil+=c.get_ht_lu();
			hiutil+=c.get_ht_lu();
		}
		return Math.max(loutil, hiutil);
	}



	public int size() {
		return g_comps.size();
	}
	
	
	// file
	public void writeFile(String file) {
		CompGenFile.writeFile(file, g_comps);
	}
	
	public void loadFile(String f) {
		g_comps=CompGenFile.loadFile(f);
	}

	public CompMng getCM() {
		CompMng cm=new CompMng();
		for(OComp c:g_comps){
//			c.prn(2);
			cm.addComp(c);
			
		}
		return cm;
	}

	
}
