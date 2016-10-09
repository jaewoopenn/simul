package comp;

import java.util.Vector;

import utilSim.FUtil;



public class OldCompGenFile {
	public static void writeFile(String file,Vector<OldComp> g_comps) {
		FUtil fu=new FUtil(file);
		for(OldComp c:g_comps)
		{
			String txt=c.getString();
			fu.print(txt);
		}
		fu.save();
	}


	public static Vector<OldComp>  loadFile(String f) {
	    Vector<OldComp> comps=new Vector<OldComp>();
	    FUtil fu=new FUtil(f);
	    fu.load();
	    for(int i=0;i<fu.size();i++){
	    	String line=fu.get(i);
//	    	Log.prn(2, line);
	    	OldComp c=new OldComp(line);
	    	comps.add(c);
	    }
	    return comps;
	}
 	
}
