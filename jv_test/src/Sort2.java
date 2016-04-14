import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;




public class Sort2 {

	public static void main(String[] args) {
		//  Auto-generated method stub
		System.out.println("hihi");
		Vector<Person> v=new Vector<Person>();
		v.add(new Person(1,11));
		v.add(new Person(1,18));
		v.add(new Person(1,15));
		v.add(new Person(1,14));
		for(Person s:v)
			System.out.println(s.age);
		System.out.println("---");
//		v.sort(new PComparator());
//		Collections.sort(v, new PComparator());
		Collections.sort(v);
//		v.sort(null);
		for(Person s:v)
			System.out.println(s.age);
	}

}
class Person implements Comparable<Person>
{
	int no;
	int age;
	public Person(int a,int b){
		no=a;
		age=b;
	}
	public int compareTo(Person o) {
		return age>o.age?1:0;
	}
}
//class PComparator implements Comparator<Person>{
//	public int compare(Person arg0, Person arg1) {
//		System.out.println( arg0.age > arg1.age ?1:0);
//		return arg0.age > arg1.age ?1:0;
//	}
//}	
