package boggle;

import java.util.ArrayList;
import java.util.Stack;

import utils.LetterGrid;

public class BoggleGame {

	/**
	 * The grid that contains all the letters. @see boggle.LetterGrid
	 */
	LetterGrid grid;

	/**
	 * The stack that stores the path when you search the word path
	 */
	Stack<String> path;

	/**
	 * A boolean array to mark any location (row,col) as visited
	 */
	boolean[][] visited;

	public BoggleGame(LetterGrid g) {
		grid = g;
	}

	private int[] colVal = { 1, 1, 1, 0, 0, -1, -1, -1 };
	private int[] rowVal = { -1, 1, 0, -1, 1, 1, -1, 0 };
	
	/**
	 * implement your method here (you may write helper method)
	 * 
	 * @param word
	 * @return true if "word" can be found in grid, false otherwise
	 */
	public boolean findWord(String word) {
		for (int row = 0; row < grid.getNumRows(); row++) {
			for (int col = 0; col < grid.getNumCols(); col++) {
				visited = new boolean[grid.getNumRows()][grid.getNumCols()];
				path = new Stack<>();
				if (findWord(word, row, col)) {
					return true;
				}

			}
		}
		return false;
	}

	private boolean findWord(String word, int row, int col) {
		if (word.equals("")) {
			return true;
		} else if (row < 0 || row >= grid.getNumRows() || col < 0 || col >= grid.getNumCols()
				|| grid.getLetter(row, col) != word.charAt(0)) {
			return false;
		} else if (grid.getLetter(row, col) == word.charAt(0) && visited[row][col] != true) {
			visited[row][col] = true;
			path.push("(" + row + "," + col + ")");
			return findWord(word.substring(1), row - 1, col - 1) || findWord(word.substring(1), row - 1, col)
					|| findWord(word.substring(1), row - 1, col + 1) || findWord(word.substring(1), row, col - 1)
					|| findWord(word.substring(1), row, col + 1) || findWord(word.substring(1), row + 1, col - 1)
					|| findWord(word.substring(1), row + 1, col) || findWord(word.substring(1), row + 1, col + 1);
		}
		return false;
	}

	/**
	 * @param word
	 * @return the path (cell index) of each letter
	 */
	public String findWordPath(String word) {
		Stack<String> myStack = new Stack<>();
		String ans = "";
		for (int row = 0; row < grid.getNumRows(); row++) {
			for (int col = 0; col < grid.getNumCols(); col++) {
				visited = new boolean[grid.getNumRows()][grid.getNumCols()];
				path = new Stack<>();
				if (findWord(word, row, col)) {
					for (int i = 0; i < word.length(); i++) {
						myStack.push(path.pop());
					}
					for (int i = 0; i < word.length(); i++) {
						ans += myStack.pop();
					}
					return ans;
				}
			}
		}

		return ans;
	}


	/**
	 * Determines the frequency of a word on the Boggle board. For simplicity,
	 * assume palindromes count twice.
	 * 
	 * @param word
	 * @return the number of occurrences of the "word" in the grid
	 */
	public int frequency(String word) {
		int count = 0;
		for (int row = 0; row < grid.getNumRows(); row++) {
			for (int col = 0; col < grid.getNumCols(); col++) {
				visited = new boolean[grid.getNumRows()][grid.getNumCols()];
				path = new Stack<>();
				if (grid.getLetter(row, col) == word.charAt(0)) {
					visited[row][col] = true;
					count += freqHelp(word.substring(1), row, col);
					visited[row][col] = false;
				}
			}
		}
		return count;
	}

	private int freqHelp(String word, int row, int col) {
		int sum = 0;
		if (word.length() == 0) {
			return 1;
		} else {
			for (int i = 0; i < 8; i++) {
				if (row + rowVal[i] < 0 || row + rowVal[i] >= grid.getNumRows() || col + colVal[i] < 0
						|| col + colVal[i] >= grid.getNumCols() || visited[row + rowVal[i]][col + colVal[i]] == true) {
				} else {
					if (grid.getLetter(row + rowVal[i], col + colVal[i]) == word.charAt(0)) {
						path.push("(" + row + rowVal[i] + "," + col + colVal[i] + ")");
						visited[row + rowVal[i]][col + colVal[i]] = true;
						sum += freqHelp(word.substring(1), row + rowVal[i], col + colVal[i]);
						visited[row + rowVal[i]][col + colVal[i]] = false;
					}
				}
			}
		}
		return sum;
	}
}
