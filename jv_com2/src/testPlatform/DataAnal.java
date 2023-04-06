package testPlatform;

import java.util.Vector;

import util.MList;
import util.SLog;

public class DataAnal {
	protected String g_path;
	protected Vector<String> g_xl=new Vector<String>();
	protected Vector<String> g_yl=new Vector<String>();
	protected String[][] g_rs;
	protected int g_class_size=0;
	protected int g_data_size=0;
	public DataAnal(String path,int class_size) {
		g_path=path;
		g_class_size=class_size;
	}
	public void load_x(String fn) {
		MList fu=new MList(g_path+"/"+fn);
		g_data_size=fu.size();
		for(int i=0;i<g_data_size;i++) {
			String s=fu.get(i);
//			SLog.prn(9, s);
			g_xl.add(s);
		}
		g_rs=new String[g_class_size][g_data_size];
	}
	public void load_rs(String fn,String yl, int class_idx){
		g_yl.add(yl);
		MList fu=new MList(g_path+"/"+fn);
		int size=fu.size();
		for(int i=0;i<size;i++) {
			String s=fu.get(i);
			g_rs[class_idx][i]=s;
		}		
	}
	

	public void save(String fn) {
		MList fu=new MList();
		String str=" ";
		for(int j=0;j<g_class_size;j++) {
			str+=","+g_yl.elementAt(j);
		}
		fu.add(str);		

		for(int i=0;i<g_data_size;i++) {
			str=g_xl.elementAt(i);
			for(int j=0;j<g_class_size;j++) {
				str+=","+g_rs[j][i];
			}
			fu.add(str);		
		}
		fu.save(g_path+"/"+fn);
	}


	
	
	public static void main(String[] args) {
		String[] lab={"alpha=0","0<alpha<=0.25","0.25<alpha<=0.5","0.5<alpha<=0.75","0.75<alpha<=1"};
		DataAnal da=new DataAnal("fc",5);
		da.load_x("rs/util_x.txt");
		for(int i=0;i<5;i++) {
			da.load_rs("rs/util_"+i+".txt",lab[i] , i);
		}
		da.save("gra/util_alpha.csv");
		
	}	
}
