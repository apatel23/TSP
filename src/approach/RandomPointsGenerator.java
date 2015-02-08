package approach;


import java.awt.Point;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;


public class RandomPointsGenerator {
	// http://moderntone.blogspot.com/2013/02/a-random-points-generator.html

	private static final Random RANDOM = new Random();
	private static final String OUTPUTFILEPATH = "randomPoints.txt";

	private int left, right, top, bottom;
	private int width, height;

	/**
	 * The four parameters represent the four edges that enclose the rectangle
	 * @param left
	 * @param right
	 * @param top
	 * @param bottom
	 */
	public RandomPointsGenerator(int left, int  right, int bottom, int top){
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
		if (top <= bottom || right <= left){
			JOptionPane.showMessageDialog(null, "Please input valid edges for the rectangle.");
			System.exit(0);
		}
		width = right - left;
		height = top - bottom;
	}

	/**
	 * get n random points inside the rectangle bounded by the four edges
	 * @param n
	 * @return points
	 */
	public ArrayList<Point> getRandomPoints(int n){  
		ArrayList<Point> points = new ArrayList<Point>();
		int x, y;
		for (int i = 0 ; i < n ; i ++){
			x = RANDOM.nextInt(100);
			y = RANDOM.nextInt(100);
			Point p = new Point(x,y);
			//System.out.println(p);
			points.add(p);
		}
		return points; 
	}

	/**
	 * output the n random points in a text file
	 * @param n
	 */
	public void outputRandomPoints(int n){
		ArrayList<Point> points = getRandomPoints(n);
		try {
			FileWriter write = new FileWriter(OUTPUTFILEPATH);
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(String.valueOf(n));
			writer.newLine();
			for (Point point : points){
				writer.write(point.x  + " " + point.y);
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There's some IO exception");
			System.exit(0);
		}
	}
}
