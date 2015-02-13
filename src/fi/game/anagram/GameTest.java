package fi.game.anagram;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GameTest {

	@Rule
	public ExpectedException ee = ExpectedException.none();
	
	@Test
	public void testCalculateScore() {
		Game mygame = new Game();
		assertEquals("Orchestra is anagram of Carthorse with score 9", 9, mygame.calculateScore("orchestra", "carthorse"));
		
		Assert.assertNotEquals("rife should be anagram of fire score should be non-zero", 0, mygame.calculateScore("rife", "fire"));
		
		ee.expect(NullPointerException.class);
		mygame.calculateScore("orchestra", null); //NullPointerException
	}

	
	@Test
	public void testSubmitScore() throws IOException {
		Game mygame = new Game();
		
		mygame.submitScore("6h", 0); //The test should fail if the exception is encountered while writing the file
	}

	@Test
	public void testGetLeaderBoard() throws NumberFormatException, IOException {
		
		Game mygame = new Game();
		List<Entry> leaderlist;
		leaderlist = mygame.getLeaderBoard("2b"); //Get the leaderboard for the uid 2b

		Assert.assertNotEquals("The list should be non-zero for existing uid", 0, leaderlist.size());
		
		leaderlist = mygame.getLeaderBoard("10z"); //For nonexistent uid
		Assert.assertEquals("for non-existent fields the list should be empty", 0, leaderlist.size());
	}

}
