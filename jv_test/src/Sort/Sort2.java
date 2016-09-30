package Sort;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;




public class Sort2 {

	public static void main(String[] args) {
		//  Auto-generated method stub
		System.out.println("hihi");
		Vector<Person> v=new Vector<Person>();
		v.add(new Person("a",11));
		v.add(new Person("b",18));
		v.add(new Person("d",15));
		v.add(new Person("c",14));
		for(Person s:v)
			System.out.println(s.name+","+s.age);
		System.out.println("---");
//		v.sort(new PComparator());
		Collections.sort(v, new Person.PComparator());
//		Collections.sort(v);
//		v.sort(null);
		for(Person s:v)
			System.out.println(s.name+","+s.age);
	}

}
class Person 
{
	String name;
	int age;
	public Person(String s,int b){
		name=s;
		age=b;
	}
	public int compareTo(Person o) {
		return age>o.age?1:0;
	}
	public static class PComparator implements Comparator<Person>{
		public int compare(Person arg0, Person arg1) {
//			System.out.println( arg0.age > arg1.age ?1:0);
//			return arg0.age > arg1.age ?1:0;
//			int det=arg0.name.compareTo(arg1.name);
			int det=arg0.age > arg1.age ?1:-1;
//			System.out.println( det);
			return det;
		}
	}
}
