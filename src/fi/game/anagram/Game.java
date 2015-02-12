package fi.game.anagram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Game {

	public static void main(String[] args) throws IOException {
		
		Game newgame = new Game();
		
		//Calculate the score
		int res;
		
		res = newgame.calculateScore("orchestra","carthorse"); 
		System.out.println(res);
		
		//res = newgame.calculateScore("orchestra",null); //NullPointerException if the string is null
		
		
		
		//Submit the score
		
		newgame.submitScore("6h", 0); //This adds the score with uid as 6h
		
		//In case when the uid is already present in the file, add the score to the existing element
		newgame.submitScore("5g", res);
		
		
		//Display Leaderboard
		List<Entry> leaderlist;
		leaderlist = newgame.getLeaderBoard("2b"); //Get the leaderboard for the uid 2b

		newgame.stringify(leaderlist); //Display it
		
		leaderlist = newgame.getLeaderBoard("10z"); //Obtain the leaderboard for non existent uid
		System.out.println(leaderlist); //Empty list

	}

	
	
	public int calculateScore(String word, String anagram)
	
	{
		
		if(word==null || anagram==null)
		{
			throw new NullPointerException();
		}
		
		String[] chars = word.split("");
		String[] inputchars = anagram.split("");
		
		HashMap<String, Integer> map = new HashMap<String,Integer>();
		
		for(int i=1;i<chars.length;i++)
		{
			
			if(!map.containsKey(chars[i]))
				map.put(chars[i], 1);
			else
			{
				int val = map.get(chars[i]) + 1;
				
				map.put(chars[i], val);
			}
			
		}
		
		
		
		HashMap<String, Integer> inputmap = new HashMap<String,Integer>();
		
		
		for(int j=1;j<inputchars.length;j++)
		{
			
			
			if(!inputmap.containsKey(inputchars[j]))
				inputmap.put(inputchars[j], 1);
			else
			{
				int val = inputmap.get(inputchars[j]) + 1;
				
				inputmap.put(inputchars[j], val);
			}
		}
		
		
		
		for(int k=1;k<inputchars.length;k++)
		{
			
			if(!map.containsKey(inputchars[k]))
				return 0;
			
			else
			{
				int inputval = inputmap.get(inputchars[k]);
				int val = map.get(inputchars[k]);
				
				if(inputval!=val)
					return 0;
			}
			
		}
		
		
		return word.length();
		
	}
	
	
	
	
	
	
	public void submitScore(String uid, int score) throws IOException
	{
	
		String line = "";
		int finalscore = 0;
		//Read the file
		File f = new File("scoreboard.txt"); //If the file does not exist then create one
		if(!f.exists())
			f.createNewFile();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("scoreboard.txt")));
		List<Entry> list = new ArrayList<Entry>();
		boolean insert = false;
		
		while((line = br.readLine())!=null)
		{
			String[] linearr = line.split(",");
			
			String localuid = linearr[0];
			
			if(localuid.equals(uid)){
				finalscore = Integer.parseInt(linearr[1]) + score; //Append the score if previous entry found
				insert = true;
			}
			else
				finalscore = Integer.parseInt(linearr[1]); //Just take the score whatever was present in the file
				
			Entry e = new Entry();
			e.setUid(localuid);
			e.setScore(finalscore);
			list.add(e);
		}
		
		if(!insert)
		{
			Entry e = new Entry();
			e.setUid(uid);
			e.setScore(score);
			list.add(e);
			
		}
		
		
		br.close();
		
		
		//Write the file
		FileWriter fw = new FileWriter("scoreboard.txt");
		Iterator<Entry> it = list.iterator();
		
		while(it.hasNext())
		{
			Entry e = it.next();
			String finalseq = e.getUid() + "," + String.valueOf(e.getScore());
			
			
			fw.append(finalseq);
			fw.append(System.lineSeparator());
		}
		
		
		fw.flush();
		fw.close();
		
		
	}
	
	
	
	public List<Entry> getLeaderBoard(String uid) throws NumberFormatException, IOException
	{
		String line = "";
		List<Entry> leaders = new ArrayList<Entry>();
		//Map<String, Entry> searchmap = new HashMap<String, Entry>(); //For easy searching
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("scoreboard.txt")));
		
		while((line = br.readLine())!=null)
		{
			String[] linearr = line.split(",");
			
			String localuid = linearr[0];
			
			int score = Integer.parseInt(linearr[1]);
			
				
				Entry entry = new Entry();
				entry.setUid(localuid);
				entry.setScore(score);
				entry.setPosition(0);
			
				leaders.add(entry);
				//searchmap.put(localuid, entry);
				
		}
		
		
		br.close();
		
		//Sorting
		
		Collections.sort(leaders, new EntryComparator());
		
		
		//For efficient searching
		Map<String,Entry> searchmap = new LinkedHashMap<String,Entry>();
		
		Iterator<Entry> i = leaders.iterator();
		
		int index = 1;
		while(i.hasNext())
		{
			Entry e = i.next();
			e.setPosition(index);
			searchmap.put(e.getUid(), e);
			index++;
			
		}
		
		
		
		List<Entry> result = new ArrayList<Entry>(); //To store the final 5 results
		
		if(searchmap.containsKey(uid))
		{
			
			Entry e = searchmap.get(uid);

			result = displayleaders(leaders,e.getPosition());
		}
		
		
		
		return result;
	}
	
	
	
	/****Used to populate the leaderboard scores*****/
	
	public List<Entry> displayleaders(List<Entry> list, int pos)
	{
		pos = pos -1;
		List<Entry> result = new ArrayList<Entry>();
		
		int first = pos - 2;
		int second = pos - 1;
		int last = pos + 2;
		int secondlast = pos + 1;
		
		//Sanity check
		if(first > 0)
			result.add(list.get(first));
		if(second > 0)
			result.add(list.get(second));
		
		result.add(list.get(pos));
		
		if(secondlast < list.size())
			result.add(list.get(secondlast));
		if(last < list.size())
			result.add(list.get(last));
		
		
		return result;
	}
	
	/****Convert the resulting list into displayable format to console ****/
	
	public void stringify(List<Entry> list)
	{
		
		Iterator<Entry> i = list.iterator();
		while(i.hasNext())
		{
			Entry e = i.next();
			System.out.println("Uid: " + e.getUid() + " Score:"  + e.getScore() + " Position: " + e.getPosition());
			
		}
		
	}
}
