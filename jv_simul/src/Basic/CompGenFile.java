package Basic;

import java.util.Vector;

import Util.FUtil;
import Util.Log;



public class CompGenFile {
	public static void writeFile(String file,Vector<Comp> g_comps) {
		FUtil fu=new FUtil(file);
		for(Comp c:g_comps)
		{
			String txt=c.getString();
			fu.print(txt);
		}
		fu.save();
	}


	public static Vector<Comp>  loadFile(String f) {
	    Vector<Comp> comps=new Vector<Comp>();
	    FUtil fu=new FUtil(f);
	    fu.load();
	    for(int i=0;i<fu.size();i++){
	    	String line=fu.get(i);
//	    	Log.prn(2, line);
	    	Comp c=new Comp(line);
	    	comps.add(c);
	    }
	    return comps;
	}
 	
}
