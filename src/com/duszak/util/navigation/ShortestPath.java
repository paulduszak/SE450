package com.duszak.util.navigation;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.duszak.facility.Facility;

public class ShortestPath {
	
	public final static int hoursPerDay = 8;
	public final static int mph = 50;
	
	private static Map<String, Facility> facilities;
	private static Map<String, Integer> pairs;
	private static HashSet<String> seen;
	private static ArrayList<String> lowPath;
	
	public ShortestPath(Map<String, Facility> facilitiesIn) {
		facilities = facilitiesIn;
		pairs = new HashMap<String, Integer>();
		seen = new HashSet<String>();
		lowPath = new ArrayList<String>();
	}
	
	public static ArrayList<String> findShortestPath(String start, String end) {
		ArrayList<String> pathList = new ArrayList<String>();
		
		mapPairs(start);
		pathList.add(start);
		findPaths(start, end, pathList);
		
		return lowPath;
	}

	private static void mapPairs(String init) {
		seen.add(init);
		List<String> neighbors = facilities.get(init).getNeighborList(); 
		
		//TODO: NULL CHECK
		
		for (String neighbor : neighbors) {
			pairs.put(init + "-" + neighbor, facilities.get(init).getNeighborDistance(neighbor));
			
			if (!seen.contains(neighbor)) 
				mapPairs(neighbor);
		}
	}
	
	private static void findPaths(String startIn, String endIn, ArrayList<String> pathListIn) {
		if(startIn.equals(endIn)) {
			if(lowPath.isEmpty()) {
				lowPath = pathListIn;
			} else {
				if(sumPath(pathListIn) < sumPath(lowPath)) {
					lowPath = pathListIn;
				}
			}
		} else {
			HashSet<String> fromHere = new HashSet<String>();
			
			for (String pair : pairs.keySet()) {
				String[] pairArray = pair.split("-");
				
				if(pairArray[0].equals(startIn)) {
					fromHere.add(pair);
				}
			}
			
			for (String pair : fromHere) {
				String[] pairArray = pair.split("-");
				
				if(!pathListIn.contains(pairArray[1])) {
					ArrayList<String> newPath = new ArrayList<String>(pathListIn);
					newPath.add(pairArray[1]);
					findPaths(pairArray[1], endIn, newPath);
				}
			}
		}		
	}

	private static int sumPath(ArrayList<String> pathListIn) {
		int sum = 0;
		
		for(int i = 0; i < pathListIn.size(); i++) {
			if (!(i+1 == pathListIn.size())) {
				sum += pairs.get(pathListIn.get(i) + "-" + pathListIn.get(i+1));
			}
		}
		
		return sum;
	}
	

	
	public String printBestPath(int hours, int mph) {
		StringBuilder s = new StringBuilder();
		int i = 1;
		int miles = sumPath(lowPath);
		float travelTime = getTravelTime(miles);
		DecimalFormat df2 = new DecimalFormat("###.##");
		s.append("   - ");
		for (String point : lowPath) {
			if (i++ == lowPath.size()) {
				s.append(point + " = " + NumberFormat.getNumberInstance(Locale.US).format(miles) + " mi");
		    } else {
		    	s.append(point + " -> ");
		    }
		}
		s.append(System.getProperty("line.separator"));
		
		s.append("   - " + miles + " mi / (" + hours + " hours per day * " + mph + " mph) = " + Double.valueOf(df2.format(travelTime)) + " days");
		return s.toString();
	}

	public static float getTravelTime(float miles) {
		return miles / (hoursPerDay*mph);
	}
	
	public static float getTravelTime(String origin, String destination) {
		ArrayList<String> sp = findShortestPath(origin, destination);
		int miles = sumPath(sp);
		
		return getTravelTime(miles);
	}
}
