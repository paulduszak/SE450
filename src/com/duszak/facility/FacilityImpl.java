package com.duszak.facility;

import java.util.ArrayList;
import java.util.Map;

import com.duszak.exception.InvalidDataException;
import com.duszak.exception.NullParameterException;
import com.duszak.facility.inventory.Inventory;
import com.duszak.facility.schedule.Schedule;
import com.duszak.facility.schedule.ScheduleFactory;
import com.duszak.util.navigation.ShortestPath;

public class FacilityImpl implements Facility {
	
	private String name;
	private int rate;
	private int cost;
	private Map<String, Integer> neighbors;
	private Map<String, Inventory> inventory;
	private Schedule schedule;
	
	public FacilityImpl(String name, int rate, int cost, Map<String, Integer> neighbors, Map<String, Inventory> inventory) throws NullParameterException, InvalidDataException {
		setName(name);
		setRate(rate);
		setCost(cost);
		setNeighbors(neighbors);
		setInventory(inventory);
		setSchedule(ScheduleFactory.createSchedule("basic", rate));		
	}
	
	public String getName() {
		return name;
	}

	public int getRate() {
		return rate;
	}

	public int getCost() {
		return cost;
	}
	
	private Map<String, Integer> getNeighbors() {
		return neighbors;
	}
	
	private void setName(String nameIn) throws NullParameterException, InvalidDataException {
		if (nameIn == null)
			throw new NullParameterException("Facility 'name' parameter cannot be null.");
		if (nameIn.length() <= 0)
			throw new InvalidDataException("Facility 'name' parameter cannot be empty string.");
		
		name = nameIn;
	}
	
	private void setRate(int rateIn) throws InvalidDataException {
		if (rateIn < 0)
			throw new InvalidDataException("Facility 'rate' parameter cannot be negative.");
		
		rate = rateIn;
	}

	private void setCost(int costIn) throws InvalidDataException {
		if (costIn < 0)
			throw new InvalidDataException("Facility 'cost' parameter cannot be negative.");
		
		cost = costIn;
	}
	
	private void setNeighbors(Map<String, Integer> neighborsIn) throws NullParameterException, InvalidDataException {
		if (neighborsIn == null)
			throw new NullParameterException("Facility 'neighbors' cannot be null.");
		
		for (String neighbor : neighborsIn.keySet()) {
			if (neighbor.length() <= 0)
				throw new InvalidDataException("Neighbor 'name' cannot be empty string.");
			if (neighborsIn.get(neighbor) < 0)
				throw new InvalidDataException("Neighbor 'distance' cannot be negative.");
		}
		
		neighbors = neighborsIn;
	}
	
	private void setInventory(Map<String, Inventory> inventoryIn) throws NullParameterException {
		if (inventoryIn == null)
			throw new NullParameterException("Facility 'inventory' cannot be null.");
		
		inventory = inventoryIn;
	}
	
	private void setSchedule(Schedule scheduleIn) throws NullParameterException {
		if (scheduleIn == null)
			throw new NullParameterException("Facility 'schedule' cannot be null.");
		
		schedule = scheduleIn;
	}
	
	
	public ArrayList<String> getNeighborList() {
		ArrayList<String> neighbors = new ArrayList<String>();
		
		for (String neighbor : getNeighbors().keySet()) {
			neighbors.add(neighbor);
		}
		
		return neighbors;
	}
	
	public int getNeighborDistance(String neighbor) {
		return getNeighbors().get(neighbor);
	}

	public ArrayList<String> getBackorderList() {
		ArrayList<String> backordered = new ArrayList<String>();
		for (Inventory item : inventory.values()) { 
			if (item.getQuantity() == 0) {
				backordered.add(item.getItemId());
			}
		}
		return backordered;
	}
	
	@Override
	public int getItemQuantity(String itemId) {
		return inventory.get(itemId).getQuantity();
	}
	
	@Override
	public void reduceItemQuantity(String itemId, int requestedQuantity) throws InvalidDataException {
		inventory.get(itemId).reduceQuantity(requestedQuantity);
	}
	
	@Override
	public boolean itemStocked(String itemId) {
		if (inventory.get(itemId) != null)
			return true;
		else
			return false;
	}
	
	private String listInventory() {
		
		StringBuilder s = new StringBuilder();
		ArrayList<String> backordered = getBackorderList();
		
		s.append(System.getProperty("line.separator"));
		s.append("Active Inventory: ");
		s.append(System.getProperty("line.separator"));
		
		for (String itemId : inventory.keySet()) {
			int quantity = inventory.get(itemId).getQuantity();
			if (quantity > 0) {
				s.append(String.format("%10s %s\n", itemId, quantity));
			}
		}
		
		s.append(System.getProperty("line.separator"));
		s.append("Depleted (Used-up) Inventory: ");
		
		if (backordered.size() == 0)
			s.append("None");
		else {
			s.append(System.getProperty("line.separator"));
			for (String itemId : backordered) {
				s.append(String.format("%10s", itemId));
				s.append(System.getProperty("line.separator"));
			}
		}
		
		s.append(System.getProperty("line.separator"));
		
		return s.toString();
	}
	
	private String listNeighbors() {
		StringBuilder s = new StringBuilder();
		s.append(System.getProperty("line.separator"));
		s.append("Direct links: ");
		s.append(System.getProperty("line.separator"));
		
		for (String name : getNeighbors().keySet()) {
			s.append(String.format("%s (%.01fd); ", name, ShortestPath.getTravelTime(getNeighbors().get(name))));
		}
		s.append(System.getProperty("line.separator"));
		
		return s.toString();
	}
	
	@Override
	public String toString() {
		String longDash = new String(new char[83]).replace("\0", "-");
		
		StringBuilder s = new StringBuilder();
		s.append(longDash + System.getProperty("line.separator"));
		
		s.append(getName());
		s.append(System.getProperty("line.separator"));
		s.append(new String(new char[getName().length()]).replace("\0", "-"));
		s.append(System.getProperty("line.separator"));
		
		s.append("Rate per Day: " + getRate());
		s.append(System.getProperty("line.separator"));
		
		s.append("Cost per Day: $" + getCost());
		s.append(System.getProperty("line.separator"));

		s.append(listNeighbors());
		
		s.append(listInventory());
		
		s.append(System.getProperty("line.separator"));
		
		s.append(schedule);
		
		s.append(longDash);
		s.append(System.getProperty("line.separator"));
		
		return s.toString();
	}

	@Override
	public int getScheduleEndDay(int orderTime, int requestedQuantity) {
		return schedule.getEndDay(orderTime, requestedQuantity);
	}

	@Override
	public float scheduleOrder(int time, int requestedQuantity) {
		return schedule.scheduleOrder(time, requestedQuantity);
	}
}
