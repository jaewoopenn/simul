package Vec;

import java.util.Vector;

public class Read1 {

	public static void main(String[] args) throws Exception {
		Vector<String> v=new Vector<String>();
		v.add("aa");
		v.add("vv");
		v.add("c");
		for(String s:v){
			System.out.println(s);
		}
//		v.remove(2);
		v.remove(v.size()-1);
		for(String s:v){
			System.out.println(s);
		}

	}

}
