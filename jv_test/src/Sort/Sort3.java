package Sort;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;




public class Sort3 {

	public static void main(String[] args) {
		Song S1 = new Song("Face to Face", "Daft Punk", 4);
		Song S2 = new Song("Chandelier", "Sia", 5);
		Song S3 = new Song("Use Somebody", "Kings Of Leon",3);
		Song S4 = new Song("Be Mine"," Ellie Goulding", 2);
		List<Song> songList = new ArrayList<Song>();
		songList.add(S1);
		songList.add(S2);
		songList.add(S3);
		songList.add(S4);
		System.out.println(" UnSorted List: " + songList);
		Collections.sort(songList);
		System.out.println("Sorted List: " + songList);
		Collections.sort(songList, new Song.SortByTitle());
		System.out.println(" Tracks sorted by Title " + songList);
		Collections.sort(songList, new Song.SortByArtist());
		System.out.println(" Tracks sorted by Artist " + songList);	
	}

}
class Song implements Comparable<Song>
{
String title;
String artist;
int rating;

public static class SortByTitle implements Comparator<Song> //inner class to sort by title
{
public int compare(Song track1, Song track2)
{
return track1.title.compareTo(track2.title);
}
}
public static class SortByArtist implements Comparator<Song> //inner class to sort by artist
{
public  int compare(Song track1, Song track2)
{
return track1.artist.compareTo(track2.artist);
}
}
public Song(String t, String a, int r) // Song constructor that takes in song details
{
this.title = t;
this.artist = a;
this.rating = r;
}
public String getTitle(){return title;}
public void setTitle(String s1){ this.title = s1;}
public String getArtist(){return artist;}
public void setArtist(String s1){this.artist = s1;}
public int compareTo(Song o)
{
return this.rating > o.rating ? 1 : (this.rating < o.rating ? -1 : 0);
}
//override toString to print sorting by rating
public String toString()
{
return String.valueOf(rating);
}
}