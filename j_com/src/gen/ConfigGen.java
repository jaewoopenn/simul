package gen;

import java.util.Arrays;
import java.util.HashMap;

import util.MFile;
import util.MList;
import util.MLoop;
import util.SLog;

public class ConfigGen {
	private final String[] g_required={"u_lb","u_ub","p_lb","p_ub",
			"tu_lb","tu_ub","num","fn","label"}; 
	private final String[] g_opt={"cu_lb","cu_ub"}; 
	private HashMap<String,String> param;
	public ConfigGen() {
		param=new HashMap<String,String>();
	}
	public String get_fn(){
		return readPar("fn");
		
	}

	public String get_lab() {
		return readPar("label");
	}

	

	public void load(String fn) {
		MFile fu=new MFile(fn);
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
	
	public boolean setParam(String field, double val){
		return setParam(field,val+"");
	}
	public boolean setParam(String field, int val){
		return setParam(field,val+"");
	}
	public boolean setParam(String field, String val){
		if(Arrays.asList(g_required).contains(field)||Arrays.asList(g_opt).contains(field)) {
			param.put(field, val);
			return true;
		}
//		SLog.prn(1, "none");
		return false;
	}
	public String readPar(String f) {
		if(Arrays.asList(g_required).contains(f)||Arrays.asList(g_opt).contains(f))
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
	public void write(String fn) {
		if(fn==null) {
			SLog.err("configGen: filename is not set");
		}
		MList fu=new MList();
		for (String s:g_required){
			String v=readPar(s);
			if(v==null){
				System.out.println("Err: required field ("+s+") is not defined");
            	return;
			}
			String txt=s+":"+v;
			fu.add(txt);
		}
		for (String s:g_opt){
			String v=readPar(s);
			if(v==null){
            	continue;
			}
			String txt=s+":"+v;
			fu.add(txt);
		}
		fu.save(fn);
	}
	public void prn(int lv) {
		SLog.prn(lv,readPar("u_ub")+"--");
	}
	public static ConfigGen getSample()	{
		ConfigGen eg=new ConfigGen();
		eg.setParam("u_lb",0.7);
		eg.setParam("u_ub",1.0);
		eg.setParam("tu_lb",0.002);
		eg.setParam("tu_ub",0.1);
		eg.setParam("p_lb",100);
		eg.setParam("p_ub",1000);
		eg.setParam("num",10);
		eg.setParam("fn","com/test1.txt");
		eg.setParam("label","1");
		return eg;
	}
	public static ConfigGen getHSample()	{
		ConfigGen eg=new ConfigGen();
		eg.setParam("u_lb",0.5);
		eg.setParam("u_ub",0.7);
		eg.setParam("tu_lb",0.002);
		eg.setParam("tu_ub",0.1);
		eg.setParam("cu_lb",0.05);
		eg.setParam("cu_ub",0.3);
		eg.setParam("p_lb",100);
		eg.setParam("p_ub",1000);
		eg.setParam("num",2);
		eg.setParam("fn","com/test1.txt");
		eg.setParam("label","1");
		return eg;
	}

	
}
