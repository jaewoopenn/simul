package gen;

import java.util.Arrays;
import java.util.HashMap;

import util.MList;
import util.SLog;
import util.MCal;

public class ConfigGen {
	private final String[] g_predefined={"u_lb","u_ub","p_lb","p_ub",
			"tu_lb","tu_ub","r_lb","r_ub","prob_hi","mo_lb","mo_ub","num","subfix","mod"}; 
	//"c_lb","c_ub",
	//"a_lb","a_ub",
	private HashMap<String,String> param;
	private String g_fn;
	public ConfigGen(String f) {
		param=new HashMap<String,String>();
		g_fn=f;
	}
	public void readFile() {
		MList fu=new MList(g_fn);
	    for(int i:MCal.loop(fu.size())){
	    	String line=fu.get(i);
            String[] words=line.split(":");
            if(words.length<2) 
            	continue;
            
            if(!setParam(words[0],words[1])) {
            	System.out.println("ERROR: loading field ("+words[0]+") is not defined");
            	System.exit(1);
            }
		}
		for (String s:g_predefined){
			if(readPar(s)==null){
				System.out.println("ERROR: required field ("+s+") is not defined");
            	System.exit(1);
			}
		}
//			Log.prn(1, s+":"+readPar(s));
	}
	public String get_mod() {
		return readPar("mod").trim();
		
	}
	public String get_fn(){
		String mod=get_mod();
		String fn="taskset_"+mod+".txt";
		return fn;
		
	}

//	public String get_dir(){
//		String subfix=readPar("subfix").trim();
//		String mod=readPar("mod").trim();
//		String fn=subfix+"/"+mod;
//		return fn;
//		
//	}
	
	
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
	public void write() {
		if(g_fn==null) {
			SLog.err("configGen: filename is not set");
		}
		MList fu=new MList();
		for (String s:g_predefined){
			String v=readPar(s);
			if(v==null){
				System.out.println("Err: required field ("+s+") is not defined");
            	return;
			}
			String txt=s+":"+v;
			fu.add(txt);
		}
		fu.saveTo(g_fn);
		
	}
	public void prn(int lv) {
		SLog.prn(lv,readPar("u_ub")+"--");
	}
	public static ConfigGen getPredefined()	{
		ConfigGen eg=new ConfigGen(null);
		eg.setParam("u_lb","0.95");
		eg.setParam("u_ub","1.0");
		eg.setParam("p_lb","20");
		eg.setParam("p_ub","150");
		eg.setParam("tu_lb","0.02");
		eg.setParam("tu_ub","0.2");
		eg.setParam("r_lb","0.25");
		eg.setParam("r_ub","1.0");
		eg.setParam("mo_lb","0.25");
		eg.setParam("mo_ub","1.0");
		eg.setParam("prob_hi","0.5");
		eg.setParam("num","10");
		eg.setParam("subfix","exp");
		eg.setParam("mod","t");
		return eg;
	}
	public static ConfigGen getPredefinedMC()	{
		ConfigGen eg=new ConfigGen(null);
		eg.setParam("u_lb","0.95");
		eg.setParam("u_ub","1.0");
		eg.setParam("p_lb","20");
		eg.setParam("p_ub","150");
		eg.setParam("tu_lb","0.02");
		eg.setParam("tu_ub","0.2");
		eg.setParam("r_lb","0.25");
		eg.setParam("r_ub","1.0");
		eg.setParam("mo_lb","0.0");
		eg.setParam("mo_ub","0.0");
		eg.setParam("prob_hi","0.5");
		eg.setParam("num","10");
		eg.setParam("subfix","exp");
		eg.setParam("mod","t");
		return eg;
	}
	public void setFile(String fn) {
		g_fn=fn;
		
	}

}
