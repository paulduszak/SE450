package com.duszak.facility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.duszak.exception.InvalidDataException;
import com.duszak.util.loader.Loader;
import com.duszak.util.loader.LoaderFactory;
import com.duszak.util.navigation.ShortestPath;

public class FacilityManager {
	
	private static FacilityManager instance;
	private static Map<String, Facility> facilities;
	private static ShortestPath sp;
	
	private FacilityManager() { 
		if (facilities == null) {
			Loader loader = LoaderFactory.createLoader("facility");
			facilities = new HashMap<String, Facility>();
			
			for (Object f : loader.load("src/data/facilities.xml")) {
				Facility facility = (Facility)f;
				facilities.put(facility.getName(), facility);
			}
			
			sp = new ShortestPath(facilities);
		}
	}
	
	public static FacilityManager getInstance() {
		if (instance == null)
			instance = new FacilityManager();
		
		return instance;
	}
	
	public static void printFacilityStatuses() {
		
		for (Facility f : facilities.values()) {
			System.out.println(f);
		}
	}
	
	public static void findShortestPath(String start, String end) {
		ShortestPath.findShortestPath(start, end);
		System.out.println(sp.printBestPath(8, 50));
	}

	public static List<String> getStockingFacilities(String destination, String itemId) {
		List<String> facilityList = new ArrayList<String>();
		
		for (Facility f : facilities.values()) {
			if (!f.getName().equals(destination)) {
				if (f.itemStocked(itemId)) {
					facilityList.add(f.getName());
				}
			}
		}
		
		return facilityList;
	}
	
	public static int getQuantityAvailableAtFacility(String facilityName, String itemId) {
		return facilities.get(facilityName).getItemQuantity(itemId);
	}
	
	public static int getFacilityCost(String facilityId) {
		return facilities.get(facilityId).getCost();
	}

	public static int getOrderEndDay(String currentFacility, int orderTime, int requestedQuantity) {
		return facilities.get(currentFacility).getScheduleEndDay(orderTime, requestedQuantity);
	}

	public static void reduceFacilityInventory(String itemId, String facilityId, int requestedQuantity) throws InvalidDataException {
		facilities.get(facilityId).reduceItemQuantity(itemId, requestedQuantity);
	}

	public static float scheduleOrder(String facilityId, int time, int requestedQuantity) {
		return facilities.get(facilityId).scheduleOrder(time, requestedQuantity);
	}
}
