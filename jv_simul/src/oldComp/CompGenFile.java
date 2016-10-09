package oldComp;

import java.util.Vector;

import utilSim.FUtil;



public class CompGenFile {
	public static void writeFile(String file,Vector<OComp> g_comps) {
		FUtil fu=new FUtil(file);
		for(OComp c:g_comps)
		{
			String txt=c.getString();
			fu.print(txt);
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
