package fi.game.anagram;

import java.util.Comparator;

public class EntryComparator implements Comparator<Entry> {

	@Override
	public int compare(Entry e1, Entry e2) {
		
		if(e1.getScore() < e2.getScore())
			return 1;
		else
			return -1;
	}

	

	
}
