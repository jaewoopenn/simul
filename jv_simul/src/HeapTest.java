import java.util.PriorityQueue;
import Util.Log;
public class HeapTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PriorityQueue<Integer> pq=new PriorityQueue<Integer>();
		pq.add(3);
		pq.add(5);
		pq.add(1);
		pq.add(4);
		int sz=pq.size();
		for(int i=0;i<sz;i++)
		{
			Log.prn(pq.poll());
		}
		Log.prn("hihi");
	}

}
