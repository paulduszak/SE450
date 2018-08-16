package com.duszak.facility;

import java.util.Map;

import com.duszak.exception.InvalidDataException;
import com.duszak.exception.NullParameterException;
import com.duszak.facility.inventory.Inventory;

public class FacilityFactory {

	private FacilityFactory() { }
	
	public static Facility createFacility(String type, String name, int rate, int cost, Map<String, Integer> neighbors, Map<String, Inventory> inventory) throws NullParameterException, InvalidDataException {
		if( type.equals("standard") ) 
			return new FacilityImpl(name, rate, cost, neighbors, inventory);
		else
			return null;
	}
}
