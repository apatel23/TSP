package approach;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class Run {

	private Run run;
	static ArrayList<Point> points;
	static ArrayList<Point> current;
	static List<List<Point>> accum;
	private static final Random RANDOM = new Random();
	private final static String file = "coordinates.txt";
	private static int numCoordinates = 0;
	
	Run() {
		run = new Run();
		current = new ArrayList<Point>();
	}
	
	public static void openFile(String file) throws IOException {
		ArrayList<Integer> coordinates = new ArrayList<Integer>();
		ArrayList<String> coors = new ArrayList<String>();
		points = new ArrayList<Point>();

		Scanner in = null;
		try {
			in = new Scanner(new FileReader(file));
			while(in.hasNext()) {
				String[] line = in.nextLine().split(" "); // separate by a space
				for(int i=0; i<line.length; i++) {
					coors.add(line[i]); // add each line to the list of strings
				}
			}
			for(String s : coors)
				coordinates.add(Integer.parseInt(s)); // convert strings to ints
			numCoordinates = coordinates.get(0);
			coordinates.remove(0);
			for(int i = 0; i < numCoordinates; i++) {
				Point point = new Point(); // each pair of ints becomes a point
				point.setLocation(coordinates.get(2*i), coordinates.get(2*i+1));
				points.add(point); 
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static double NearestNeighbor(ArrayList<Point> points) {
		double totDistance = 0;
		double distance = 0;
		Point p0 = points.get(0); // starting point
		Point current = p0; 
		Point closest = new Point(); // closest point to the current point
		ArrayList<Point> unvisited = points;
		unvisited.remove(0); // remove the first point
		int remain = numCoordinates;
		while(remain > 0) {
			double min = 100000;
			for(int j=0; j<unvisited.size(); j++) {
				distance = points.get(j).distance(current.getX(), current.getY());
				//System.out.println("dist from " + points.get(j) + " to " + current + " is " + distance  );
				if(distance < min) {
					min = distance;
					closest = points.get(j);
					//System.out.println("closest point to " + current + " is " + points.get(j) );
				}
			}
			remain--;
			//System.out.println("goto " + closest + " from " + current);
			totDistance += closest.distance(current);
			current = closest;
			unvisited.remove(current);
			for(Point p : unvisited) {
				//System.out.println("P: " + p);
			}
		}
		totDistance += p0.distance(current.getX(), current.getY()); // back to the initial point
		//System.out.println("goto " + p0 + " from " + current);
		System.out.println("nearest neighbors path: " + totDistance);
		
		return totDistance;
	}
	
	public static double Optimal(ArrayList<Point> points) {
		double totDistance = 0;
		double distance = 0;
		double min = 10000;
		permutation(points);
		Point last = new Point();
		for(int i = 0; i < accum.size(); i++) {
			totDistance = 0;
			//System.out.println("current: " + accum.get(i));
			for(int j = 1; j < numCoordinates; j++) {
				last = accum.get(i).get(j-1);
				//System.out.println(last + " to " + accum.get(i).get(j));
				totDistance += accum.get(i).get(j).distance(last);
			}
			totDistance += accum.get(i).get(0).distance(last);
			//System.out.println(totDistance);
			if(totDistance < min) {
				min = totDistance;
				List<Point> shortest = new ArrayList();
				shortest = accum.get(i);
				//System.out.println("shortest: " + shortest + " w/ dist " + totDistance);
			}
		}
		System.out.println("Optimal path: " + min);
		return totDistance;
	}
	
	
	// http://stackoverflow.com/questions/25704754/get-arraylist-of-all-possible-permutations-of-an-arraylist
	// Used in the optimal TSP algorithm
	public static void permutation(List<Point> nums) {
	    accum = new ArrayList<List<Point>>();
	    permutation(accum, Arrays.<Point>asList(), nums);
	    for(List<Point> p : accum) {
	    	//System.out.println(p); // prints all permutations
	    }
	}

	// Used in the optimal TSP algorithm
	private static void permutation(List<List<Point>> accum, List<Point> prefix, List<Point> nums) {
	    int n = nums.size();
	    if (n == 0) {
	        accum.add(prefix);
	    } else {
	        for (int i = 0; i < n; ++i) {
	            List<Point> newPrefix = new ArrayList<Point>();
	            //System.out.println((prefix));
	            //System.out.println(nums.get(i));
	            newPrefix.addAll(prefix);
	            newPrefix.add(nums.get(i));
	            List<Point> numsLeft = new ArrayList<Point>();
	            numsLeft.addAll(nums);
	            numsLeft.remove(i);
	            permutation(accum, newPrefix, numsLeft);
	        }
	    }
	}

	
	
	public static void main(String[] args) throws IOException {
		long lStartTime; 
		long lEndTime; 
		long difference;
		
		System.out.println("n = 4: ");
		// NearestNeighbor, n = 4
		openFile(file);
		lStartTime = System.nanoTime();
		NearestNeighbor(points);
		lEndTime = System.nanoTime();
		difference = lEndTime - lStartTime;
		System.out.println("Elapsed milliseconds: " + difference);
		
		// Optimal TSP, n = 4
		openFile(file);
		lStartTime = System.nanoTime();
		Optimal(points);
		lEndTime = System.nanoTime();
		difference = lEndTime - lStartTime;
		System.out.println("Elapsed milliseconds: " + difference);
		System.out.println("---------------------------------------");
		
		/**********************************/
		// Run #1
		int rand0 = RANDOM.nextInt(10) + 1;
		System.out.println("1st run: n = " + rand0);
		RandomPointsGenerator generator = new RandomPointsGenerator(0, 100, 0, 100);
		generator.outputRandomPoints(10);
		
		// NearestNeighbor
		openFile("randomPoints.txt");
		lStartTime = System.nanoTime();
		NearestNeighbor(points);
		lEndTime = System.nanoTime();
		difference = lEndTime - lStartTime;
		System.out.println("Elapsed milliseconds: " + difference);
		
		// Optimal TSP
		openFile("randomPoints.txt");
		lStartTime = System.nanoTime();
		Optimal(points);
		lEndTime = System.nanoTime();
		difference = lEndTime - lStartTime;
		System.out.println("Elapsed milliseconds: " + difference);
		System.out.println("---------------------------------------");
		
		/**********************************/
		// Run #2
		System.out.println("2nd run: n = " + rand0);
		
		// NearestNeighbor
		openFile("randomPoints.txt");
		lStartTime = System.nanoTime();
		NearestNeighbor(points);
		lEndTime = System.nanoTime();
		difference = lEndTime - lStartTime;
		System.out.println("Elapsed milliseconds: " + difference);
		
		// Optimal TSP
		openFile("randomPoints.txt");
		lStartTime = System.nanoTime();
		Optimal(points);
		lEndTime = System.nanoTime();
		difference = lEndTime - lStartTime;
		System.out.println("Elapsed milliseconds: " + difference);
		System.out.println("---------------------------------------");
		
		/**********************************/
		// Run #3
		System.out.println("3rd run: n = " + rand0);
		
		// NearestNeighbor
		openFile("randomPoints.txt");
		lStartTime = System.nanoTime();
		NearestNeighbor(points);
		lEndTime = System.nanoTime();
		difference = lEndTime - lStartTime;
		System.out.println("Elapsed milliseconds: " + difference);
		
		// Optimal TSP
		openFile("randomPoints.txt");
		lStartTime = System.nanoTime();
		Optimal(points);
		lEndTime = System.nanoTime();
		difference = lEndTime - lStartTime;
		System.out.println("Elapsed milliseconds: " + difference);
		System.out.println("---------------------------------------");

	}

}
