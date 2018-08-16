package com.duszak.facility;

import java.util.List;

import com.duszak.exception.InvalidDataException;

public interface Facility {

	public String getName();
	public int getRate();
	public int getCost();
	
	public boolean itemStocked(String itemId);
	public int getItemQuantity(String itemId);	
	public void reduceItemQuantity(String itemId, int requestedQuantity) throws InvalidDataException;
	
	public List<String> getBackorderList();
	
	public List<String> getNeighborList();
	public int getNeighborDistance(String neighbor);
	public int getScheduleEndDay(int orderTime, int requestedQuantity);
	public float scheduleOrder(int time, int requestedQuantity);
	
}
