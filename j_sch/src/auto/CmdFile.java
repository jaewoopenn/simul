package auto;

import java.util.HashMap;
import java.util.Vector;

import util.FOut;
import util.FUtilSp;
import util.S_Log;
import util.MUtil;

public class CmdFile {
	private HashMap<String,String> g_param;
	private Vector<String> g_list;
	private String g_fn;
	public CmdFile(String f) {
		g_param=new HashMap<String,String>();
		g_list=new Vector<String>();
		g_fn=f;
	}
	public void readFile() {
		FUtilSp fu=new FUtilSp(g_fn);
	    fu.load();
	    for(int i:MUtil.loop(fu.size())){
	    	String line=fu.get(i);
            String[] words=line.split(":");
            if(words.length<2) 
            	continue;
            
            if(!setParam(words[0],words[1])) {
            	System.out.println("ERROR: loading field ("+words[0]+") is not defined");
            	System.exit(1);
            }
		}
//			Log.prn(1, s+":"+readPar(s));
	}
	
	
	public boolean setParam(String field, String val){
		if(!g_list.contains(field))
			g_list.add(field);
		g_param.put(field, val);
		return true;
	}
	public String readPar(String f) {
		return g_param.get(f);
	}
	public int readInt(String f){
		String s=readPar(f);
		if(s==null)
			return -1;
		return Integer.valueOf(s.trim()).intValue();
	}
	public double readDbl(String f){
		String s=readPar(f);
		if(s==null)
			return -1;
		return Double.valueOf(s.trim()).doubleValue();
	}
	public void write() {
		FOut fu=new FOut(g_fn);
		for (String s:g_list){
			String v=readPar(s);
			if(v==null){
				System.out.println("Err: required field ("+s+") is not defined");
            	return;
			}
			String txt=s+":"+v;
			fu.write(txt);
		}
		fu.save();
		
	}
	public void prn() {
		for (String s:g_list){
			String v=readPar(s);
			S_Log.prn(1, s+ " -- "+ v);
		}
		
	}

}
