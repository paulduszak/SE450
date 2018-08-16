package com.duszak.order;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import com.duszak.exception.InvalidDataException;
import com.duszak.facility.FacilityManager;
import com.duszak.facility.FacilityRecord;
import com.duszak.item.ItemCatalog;
import com.duszak.util.loader.Loader;
import com.duszak.util.loader.LoaderFactory;
import com.duszak.util.navigation.ShortestPath;

public class OrderProcessor {

	private static OrderProcessor instance;
	private static Map<String, Order> orders;
	
	private static final float travelCost = 500;
	
	private OrderProcessor() { 
		if (orders == null) {
			Loader loader = LoaderFactory.createLoader("order");
			orders = new TreeMap<String, Order>();
			
			for (Object o : loader.load("src/data/orders.xml")) {
				Order order = (Order)o;
				orders.put(order.getId(), order);
			}
		}
	}
	
	public static OrderProcessor getInstance() {
		if (instance == null)
			instance = new OrderProcessor();
		
		return instance;
	}
	
	public static void processOrders() {
		
		int orderNum = 1; 
		String longDash = new String(new char[83]).replace("\0", "-");
		
		for (String orderId : orders.keySet()) {
			System.out.println(longDash);
			
			System.out.println("Order #:" + orderNum);
			System.out.println(orders.get(orderId));
			
			System.out.println("Processing Solution:\n");
			processOrder(orders.get(orderId));
			System.out.println(longDash);
			orderNum++;
		}
		
	}

	private static void processOrder(Order order) {
		for (String itemId : order.getItemsCopy().keySet()) {
			if (ItemCatalog.itemExists(itemId)) {
				
				int requiredQuantity = order.getItemsCopy().get(itemId);
				List<String> availableFacilities = FacilityManager.getStockingFacilities(order.getDestination(), itemId);
				List<FacilityRecord> solutions = new ArrayList<FacilityRecord>();
				
				if (availableFacilities.size() > 0) {
					while (requiredQuantity > 0) {
						
						List<FacilityRecord> facilityRecords = new ArrayList<FacilityRecord>();
					
						for (String currentFacility : availableFacilities) {
							int requestedQuantity, facilityQuantity, processingEndDay, arrivalDay;
							float travelTime;
							
							facilityQuantity = FacilityManager.getQuantityAvailableAtFacility(currentFacility, itemId);
							
							if (facilityQuantity > 0) {
								if (facilityQuantity > requiredQuantity)
									requestedQuantity = requiredQuantity;
								else
									requestedQuantity = facilityQuantity;
								
								travelTime = ShortestPath.getTravelTime(currentFacility, order.getDestination());
								processingEndDay = FacilityManager.getOrderEndDay(currentFacility, order.getTime(), requestedQuantity);
								arrivalDay = (int) Math.ceil(travelTime + processingEndDay);
								
								facilityRecords.add(new FacilityRecord(currentFacility, requestedQuantity, processingEndDay, travelTime, arrivalDay));
							}
						}
						
						Collections.sort(facilityRecords);
						
						FacilityRecord top = facilityRecords.get(0);			
						requiredQuantity -= top.requestedQuantity;
						float daysSpentProcessing = FacilityManager.scheduleOrder(top.facilityId, order.getTime(), top.requestedQuantity);
						
						try {
							FacilityManager.reduceFacilityInventory(itemId, top.facilityId, top.requestedQuantity);
						} catch (InvalidDataException e) {
							System.err.println(order.getId() + ": Cannot reduce " + top.facilityId + " quantity for item ID: " + itemId);
							e.printStackTrace();
						}
						
						//cost calc
						float itemCost = ItemCatalog.getItemCost(itemId) * top.requestedQuantity;
						float facilityCost = daysSpentProcessing * FacilityManager.getFacilityCost(top.facilityId);
						int shippingCost = (int) Math.ceil(top.travelTime * travelCost);
						top.calculateCost(itemCost, facilityCost, shippingCost);
						
						solutions.add(top);
					}
					printItemSolution(itemId, order.getItemsCopy().get(itemId), solutions);
				} else {
					System.out.println("The requested item is no longer stocked at any facility.");
				}
				
				
			} else {
				System.out.println("The ordered item does not exist in the item catalog.");
			}
			System.out.println("");
		}	
	}

	private static int printItemSolution(String itemId, int quantity, List<FacilityRecord> solutions) {
		System.out.println(itemId + ":");
		System.out.println(String.format("\t   %-20s%-10s%15s%20s", "Facility", "Quantity", "Cost", "Arrival Date"));
		
		int count = 1;
		int totalQuantity = 0;
		int totalCost = 0;
		int minDate = 200;
		int maxDate = 0;
		
		for (FacilityRecord fr : solutions) {
			System.out.println(String.format("\t%s) %-20s%-10s%15s%20s", count, fr.facilityId, fr.requestedQuantity, "$" + NumberFormat.getNumberInstance(Locale.US).format(fr.cost), fr.arrivalDay));
			totalQuantity += fr.requestedQuantity;
			totalCost += fr.cost;
			if (fr.arrivalDay < minDate)
				minDate = fr.arrivalDay;
				
			if (fr.arrivalDay > maxDate)
				maxDate = fr.arrivalDay;
			
			count++;
		}
		System.out.println(String.format("\t   %-20s%-10s%15s%20s", "TOTAL", totalQuantity, "$" + NumberFormat.getNumberInstance(Locale.US).format(totalCost), "[" + minDate + " - " + maxDate + "]"));
		
		return totalCost;
	}
}
