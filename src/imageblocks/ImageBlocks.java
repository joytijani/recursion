
package imageblocks;

import java.awt.Color;

import utils.Picture;

public class ImageBlocks {
	static Color BLACK = new Color(0, 0, 0);
	static Color WHITE = new Color(255, 255, 255);
	private int height;
	private int width;
	boolean[][] visited;
	Picture pic;

	public ImageBlocks(Picture pic) {
		this.pic = pic;
		height = pic.height();
		width = pic.width();
	}

	private int[] colVal = { 1, 1, 1, 0, 0, -1, -1, -1 };
	private int[] rowVal = { -1, 1, 0, -1, 1, 1, -1, 0 };

	private boolean isBlack(int x, int y) {
		return pic.get(x, y).equals(BLACK);
	}

	private boolean isWhite(int x, int y) {
		return pic.get(x, y).equals(WHITE);
	}

	/**
	 * count the number of image blocks in the given image Counts the number of
	 * connected blocks in the binary digital image
	 * 
	 * @return number of black blocks
	 */
	public int countConnectedBlocks() {
		visited = new boolean[height][width];
		int count = 0;
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if (isBlack(col, row) && visited[row][col] == false) {
					countConnect(row, col);
					count++;
				}
			}
		}
		return count;
	}

	private void countConnect(int row, int col) {
		visited[row][col] = true;

		// Checks 8 pixels around seed and if they are black and hasn't been visited
		// calls the recursive method again
		// Recursion will stop when a black block is completely surrounded by white
		// pixels
		for (int i = 0; i < 8; i++) {
			if (col + colVal[i] >= 0 && row + rowVal[i] >= 0 && col + colVal[i] < width && row + rowVal[i] < height
					&& isBlack(col, row) && visited[row + rowVal[i]][col + colVal[i]] == false) {
				countConnect(row + rowVal[i], col + colVal[i]);
			}
		}

	}

	/**
	 * Removes all neighboring black pixels of the provided pixel (x,y)
	 * 
	 * @param x
	 * @param y
	 * @return updated picture
	 */
	public Picture delete(int x, int y) {
		// checks the bounds of the image and then if it's black calls the helper method
		// on it
		if (x >= 0 && x < width && y >= 0 && y < height) {
			if (isBlack(x, y)) {
				deleteHelp(x, y, x, y);
			}
		}
		return pic;
	}

	// the helper method sets the pixel passed in to white
	private void deleteHelp(int row, int col, int rowStart, int colStart) {
		pic.set(row, col, WHITE);

		// Checks 8 pixels around seed and if they are black and in bounds will call the
		// recursive method on that pixel now
		// Recursion will stop when a black block is completely white now
		for (int i = 0; i < 8; i++) {
			if (col + colVal[i] >= 0 && row + rowVal[i] >= 0 && colStart + colVal[i] < width
					&& rowStart + rowVal[i] < height && isBlack(col, row)) {
				deleteHelp(row + rowVal[i], col + colVal[i], rowStart, colStart);
			}
		}

	}

	/**
	 * Crops an image by setting all the pixels outside the provided indices to the
	 * color white delete everything not inside the bounds of the parameters
	 * (inclusive)
	 * 
	 * @param row_start
	 * @param col_start
	 * @param row_end
	 * @param col_end
	 * @return updated picture
	 */
	public Picture crop(int x_start, int x_end, int y_start, int y_end) {
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				cropHelp(x_start, x_end, y_start, y_end, col, row);
			}
		}
		return pic;
	}

	private void cropHelp(int x_start, int x_end, int y_start, int y_end, int col, int row) {
		//Checks if the col and row is in the bounds
		if (col >= x_start && col <= x_end && row >= y_start && row <= y_end) {
			return;
		} else {
			// if it's in the bounds of what is to be deleted and isBlack, it sets it do white instead
			if (inBound(col, row) && isBlack(col, row)) {
				pic.set(col, row, WHITE);
			} else {
				return;
			}
			//continues to call the cropHelp method for the 8 pixels around that one
			for (int i = 0; i < 8; i++) {
				cropHelp(x_start, x_end, y_start, y_end, col + colVal[i], row + rowVal[i]);
			}
		}
	}

	//Checks if the pixel is in the bounds of the image 
	private boolean inBound(int col, int row) {
		if (col >= 0 && col < width && row >= 0 && row < height) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {

		String fileName = "images/14.png";
		Picture p = new Picture(fileName);
		ImageBlocks block = new ImageBlocks(p);
		try {
			int c1 = block.countConnectedBlocks();
			block.delete(4, 8);
			int c2 = block.countConnectedBlocks();
			p = block.crop(0, 90, 0, 90);
			int c3 = block.countConnectedBlocks();
			System.out.println("Number of connected blocks=" + c1);
			System.out.println("Number of connected blocks after delete=" + c2);
			System.out.println("Number of connected blocks after crop=" + c3);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
