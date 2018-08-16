package com.duszak.order;

import java.util.HashMap;
import java.util.Map;

import com.duszak.exception.InvalidDataException;
import com.duszak.exception.NullParameterException;

public class OrderImpl implements Order {
	
	private String id;
	private int time;
	private String destination;
	private Map<String, Integer> items;

	public OrderImpl(String id, int time, String destination, Map<String, Integer> items) throws NullParameterException, InvalidDataException {
		setId(id);
		setTime(time);
		setDestination(destination);
		setItems(items);	
	}

	private void setId(String idIn) throws NullParameterException, InvalidDataException {
		if (idIn == null)
			throw new NullParameterException("Order 'id' parameter cannot be null.");
		if (idIn.length() <= 0)
			throw new InvalidDataException("Order 'id' parameter cannot be empty string.");
		
		id = idIn;
	}
	
	private void setTime(int timeIn) throws InvalidDataException {
		if (timeIn < 0)
			throw new InvalidDataException("Order 'time' parameter cannot be negative.");
		
		time = timeIn;
	}
	
	private void setDestination(String destinationIn) throws NullParameterException, InvalidDataException {
		if (destinationIn == null)
			throw new NullParameterException("Order 'destination' parameter cannot be null.");
		if (destinationIn.length() <= 0)
			throw new InvalidDataException("Order 'destination' parameter cannot be empty string.");
		
		destination = destinationIn;
	}
	
	private void setItems(Map<String, Integer> itemsIn) throws NullParameterException {
		if (itemsIn == null)
			throw new NullParameterException("Order 'items' parameter cannot be null.");
		
		items = itemsIn;
	}
	
	public String getId() {
		return id;
	}
	
	public int getTime() {
		return time;
	}
	
	public String getDestination() {
		return destination;
	}
	
	public Map<String, Integer> getItemsCopy() {
		
		Map<String, Integer> copy = new HashMap<String, Integer>(); 
		copy.putAll(items);
		
		return copy;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		
		s.append(String.format("  %-13s%-24s", "Order Id:", getId()));
		s.append(System.getProperty("line.separator"));
		
		s.append(String.format("  %-13s%-24s", "Order Time:", "Day " + getTime()));
		s.append(System.getProperty("line.separator"));
		
		s.append(String.format("  %-13s%-24s", "Destination:", getDestination()));
		s.append(System.getProperty("line.separator"));
		
		s.append("\n  List of order items:");
		s.append(System.getProperty("line.separator"));
		
		int itemCount = 1;
		
		for (String itemId : items.keySet()) {
			s.append(String.format("  \t%s %s,\t%s", itemCount + ")", "Item ID: " + itemId, "Quantity " + items.get(itemId)));
			s.append(System.getProperty("line.separator"));
			itemCount++;
		}
		
		return s.toString();
	}
}
