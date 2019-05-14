package gen;

import java.util.Arrays;
import java.util.HashMap;

import util.FOut;
import util.FUtilSp;
import util.MLoop;
import util.S_Log;

public class ConfigGen {
	private final String[] g_required={"u_lb","u_ub","p_lb","p_ub",
			"tu_lb","tu_ub","num","fn","label"}; 
	//"c_lb","c_ub",
	//"a_lb","a_ub",
	private HashMap<String,String> param;
	public ConfigGen() {
		param=new HashMap<String,String>();
	}
	public String get_fn(){
		return readPar("fn");
		
	}

	public String getLabel() {
		return readPar("label");
	}

	
	public boolean setParam(String field, String val){
		if(Arrays.asList(g_required).contains(field)){
			param.put(field, val);
			return true;
		}
		return false;
	}
	
	public String readPar(String f) {
		if(Arrays.asList(g_required).contains(f))
			return param.get(f).trim();
		System.out.println("ERROR: requested field ("+f+") is not defined");
		return null;
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
	
	public void write(String fn) {
		if(fn==null) {
			S_Log.err("configGen: filename is not set");
		}
		FOut fu=new FOut(fn);
		for (String s:g_required){
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
	public void load(String fn) {
		FUtilSp fu=new FUtilSp(fn);
	    fu.load();
	    for(int i:MLoop.run(fu.size())){
	    	String line=fu.get(i);
            String[] words=line.split(":");
            if(words.length<2) 
            	continue;
            
            if(!setParam(words[0],words[1])) {
            	System.out.println("ERROR: loading field ("+words[0]+") is not defined");
            	System.exit(1);
            }
		}
		for (String s:g_required){
			if(readPar(s)==null){
				System.out.println("ERROR: required field ("+s+") is not defined");
            	System.exit(1);
			}
		}
//			Log.prn(1, s+":"+readPar(s));
	}
	
	public void prn(int lv) {
		S_Log.prn(lv,readPar("u_ub")+"--");
	}
	public static ConfigGen getSample()	{
		ConfigGen eg=new ConfigGen();
		eg.setParam("u_lb","0.7");
		eg.setParam("u_ub","1.0");
		eg.setParam("tu_lb","0.02");
		eg.setParam("tu_ub","0.1");
		eg.setParam("p_lb","50");
		eg.setParam("p_ub","300");
		eg.setParam("num","10");
		eg.setParam("a_lb","0.0");
		eg.setParam("a_ub","0.3");
		eg.setParam("fn","com/test1");
		eg.setParam("label","1");
		return eg;
	}

}
