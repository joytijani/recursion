package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import boggle.BoggleGame;
import utils.LetterGrid;
import utils.SimpleGrid;

public class StudentTest {

	@Test
	public void findWordFreq() {
		BoggleGame game = new BoggleGame(SimpleGrid.createSimpleGridTwo());
		System.out.println(game.frequency("ABC"));
	}

}
