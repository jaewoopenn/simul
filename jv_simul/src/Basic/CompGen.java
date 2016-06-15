package Basic;

import java.util.Vector;

import Util.Log;

public class CompGen {
	private CompGenParam g_param;
	private Vector<Comp> g_comps;
	
	public CompGen(CompGenParam param) {
		g_param=param;
	}

	public void generate() {
		while(true){
			g_comps=new Vector<Comp>();
			genSys();
			if(check()==1) break;
		}
	}
	private void genSys()
	{
		int tid=0;
		Comp t;
		while(getMCUtil()<=g_param.get_util_u()){
			t=genComp(tid);
			g_comps.addElement(t);
			tid++;
		}
		g_comps.remove(g_comps.size()-1);
	}
	

	public Comp genComp(int tid) {
		Comp tsk=g_param.genComp(tid);
		return tsk;
	}
	


	public int check(){
		return g_param.check(getMCUtil());
	}
	
	// getting
	public void prn(int lv) {
		for(Comp c:g_comps) {
			c.prn(lv);
		}
		Log.prn(lv, "MC util:"+getMCUtil());
			
	}


	
	public double getMCUtil(){
		double loutil=0;
		double hiutil=0;
		for(Comp c:g_comps){
			loutil+=c.get_lt_lu();
			loutil+=c.get_ht_lu();
			hiutil+=c.get_ht_lu();
		}
		return Math.max(loutil, hiutil);
	}

//	public Vector<Comp> getAll() {
//		return g_comps;
//	}


	public int size() {
		return g_comps.size();
	}
	
	
	// file
	public void writeFile(String file) {
//		CompGenFile.writeFile(file, g_comps);
	}
	
	public void loadFile(String f) {
//		g_comps=CompGenFile.loadFile(f);
	}

	
}
