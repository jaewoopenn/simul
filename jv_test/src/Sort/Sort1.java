package Sort;
import java.util.Collections;
import java.util.Vector;



public class Sort1 {

	public static void main(String[] args) {
		//  Auto-generated method stub
		System.out.println("hihi");
		Vector<String> v=new Vector<String>();
		v.add("aa");
		v.add("cc");
		v.add("bb");
		for(String s:v)
			System.out.println(s);
		System.out.println("---");
		Collections.sort(v);
		for(String s:v)
			System.out.println(s);
	}

}
