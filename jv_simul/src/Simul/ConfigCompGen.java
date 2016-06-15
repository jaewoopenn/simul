package Simul;

import java.util.Arrays;
import java.util.HashMap;

import Util.FUtil;

public class ConfigCompGen {
	private final String[] g_predefined={"u_lb","u_ub","lt_lu_lb","lt_lu_ub",
			"ht_lu_lb","ht_lu_ub","r_lb","r_ub","num","subfix","mod"};
	private HashMap<String,String> param;
	private String g_fn;
	public ConfigCompGen(String string) {
		param=new HashMap<String,String>();
		g_fn=string;
	}
	public int readFile() {
	    FUtil fu=new FUtil(g_fn);
	    fu.load();
	    for(int i=0;i<fu.size();i++){
	    	String line=fu.get(i);
            String[] words=line.split(":");
            if(words.length<2) 
            	continue;
            
            if(!setParam(words[0],words[1])) {
            	System.out.println("Err: loading field ("+words[0]+") is not defined");
            	return 0;
            }
		}
		for (String s:g_predefined){
			if(readPar(s)==null){
				System.out.println("Err: required field ("+s+") is not defined");
            	return 0;
			}
		}
//			Log.prn(1, s+":"+readPar(s));
		return 1;
	}
	public boolean setParam(String field, String val){
		if(Arrays.asList(g_predefined).contains(field)){
			param.put(field, val);
			return true;
		}
		return false;
	}
	public String readPar(String f) {
		if(Arrays.asList(g_predefined).contains(f))
			return param.get(f);
		System.out.println("ERR: requested field ("+f+") is not defined");
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
	public void write(String file) {
		FUtil fu=new FUtil(file);
		for (String s:g_predefined){
			String v=readPar(s);
			if(v==null){
				System.out.println("Err: required field ("+s+") is not defined");
            	return;
			}
			String txt=s+":"+v;
			fu.print(txt);
		}
		fu.save();
		
	}

}
