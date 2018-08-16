package com.duszak;

import com.duszak.facility.FacilityManager;
import com.duszak.item.ItemCatalog;
import com.duszak.order.OrderProcessor;

public class main {

	public static void main(String[] args) {
		
		ItemCatalog.getInstance();
		FacilityManager.getInstance();
		OrderProcessor.getInstance();
		
		System.out.println("Pre-ordering:\n");
		FacilityManager.printFacilityStatuses();
		
		System.out.println("Ordering:\n");
		OrderProcessor.processOrders();
		
		System.out.println("Post-ordering:\n");
		FacilityManager.printFacilityStatuses();
		
	}

}
