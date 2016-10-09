package comp;

import java.util.Vector;

import utilSim.Log;

public class CompGen {
	private CompGenParam g_param;
	private Vector<OldComp> g_comps;
	
	public CompGen(CompGenParam param) {
		g_param=param;
	}

	public void generate() {
		while(true){
			g_comps=new Vector<OldComp>();
			genSys();
			if(check()==1) break;
		}
	}
	private void genSys()
	{
		int tid=0;
		OldComp t;
		while(getMCUtil()<=g_param.get_util_u()){
			t=genComp(tid);
			g_comps.addElement(t);
			tid++;
		}
		g_comps.remove(g_comps.size()-1);
	}
	

	public OldComp genComp(int tid) {
		OldComp tsk=g_param.genComp(tid);
		return tsk;
	}
	


	public int check(){
		return g_param.check(getMCUtil());
	}
	
	// getting
	public void prn(int lv) {
		for(OldComp c:g_comps) {
			c.prn(lv);
		}
		Log.prn(lv, "MC util:"+getMCUtil());
			
	}


	
	public double getMCUtil(){
		double loutil=0;
		double hiutil=0;
		for(OldComp c:g_comps){
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
		OldCompGenFile.writeFile(file, g_comps);
	}
	
	public void loadFile(String f) {
		g_comps=OldCompGenFile.loadFile(f);
	}

	public OldCompMng getCM() {
		OldCompMng cm=new OldCompMng();
		for(OldComp c:g_comps){
//			c.prn(2);
			cm.addComp(c);
			
		}
		return cm;
	}

	
}
