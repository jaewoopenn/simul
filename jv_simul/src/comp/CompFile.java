package comp;

import java.util.Vector;

import oldComp.OComp;
import utilSim.FUtil;



public class CompFile {
	public static void writeFile(String file,Vector<Comp> g_comps) {
		FUtil fu=new FUtil(file);
		for(Comp c:g_comps)
		{
//			c
//			fu.print(txt);
		}
		fu.save();
	}


	public static Vector<OComp>  loadFile(String f) {
	    Vector<OComp> comps=new Vector<OComp>();
	    FUtil fu=new FUtil(f);
	    fu.load();
	    for(int i=0;i<fu.size();i++){
	    	String line=fu.get(i);
//	    	Log.prn(2, line);
	    	OComp c=new OComp(line);
	    	comps.add(c);
	    }
	    return comps;
	}
 	
}
