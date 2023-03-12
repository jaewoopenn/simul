package gen;

import java.util.Arrays;
import java.util.HashMap;

import util.SLog;
import util.MList;
import util.MUtil;

public class ConfigGen {
	private final String[] g_predefined={"u_lb","u_ub","p_lb","p_ub",
	"c_lb","c_ub",
	"a_lb","a_ub",
	"tu_lb","tu_ub","r_lb","r_ub","prob_hi","num","subfix","mod"}; 

	private HashMap<String,String> param;
	private String g_fn;
	public ConfigGen(String f) {
		param=new HashMap<String,String>();
		g_fn=f;
	}
	public ConfigGen() {
		param=new HashMap<String,String>();
	}
	public void readFile() {
	    MList fu=new MList(g_fn);
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
		for (String s:g_predefined){
			if(readPar(s)==null){
				System.out.println("ERROR: required field ("+s+") is not defined");
            	System.exit(1);
			}
		}
//			Log.prn(1, s+":"+readPar(s));
	}
	public String get_fn(int i){
		String subfix=readPar("subfix").trim();
		String mod=readPar("mod").trim();
		String fn=subfix+"/"+mod+"/taskset_"+i;
		return fn;
		
	}
	public String get_dir(){
		String subfix=readPar("subfix").trim();
		String mod=readPar("mod").trim();
		String fn=subfix+"/"+mod;
		return fn;
		
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
	public void write(String file) {
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
		fu.save(file);
		
	}
	public void genRange(String fn,int start, int step, int size) {
		for(int i=0;i<size;i++){
			setParam("u_lb", (i*step+start)*1.0/100+"");
			setParam("u_ub", (i*step+start+5)*1.0/100+"");
			setParam("mod", (i*step+start+5)+"");
			write(fn+"_"+i+".txt");
		}
	}
	public void prn(int lv) {
		SLog.prn(lv,readPar("u_ub")+"--");
	}
	public int getSize() {
		return readInt("num");
	}
	public static ConfigGen getCfg()	{
		ConfigGen eg=new ConfigGen("");
		eg.setParam("u_lb","0.95");
		eg.setParam("u_ub","1.0");
		eg.setParam("c_lb", "0.1");
		eg.setParam("c_ub", "0.3");
		eg.setParam("p_lb","50");
		eg.setParam("p_ub","300");
		eg.setParam("tu_lb","0.02");
		eg.setParam("tu_ub","0.1");
		eg.setParam("r_lb","0.25");
		eg.setParam("r_ub","1.0");
		eg.setParam("prob_hi","0.5");
		eg.setParam("num","10");
		eg.setParam("a_lb","0.1");
		eg.setParam("a_ub","0.3");
		eg.setParam("subfix","fc");
		eg.setParam("mod","t");
		return eg;
	}

}
