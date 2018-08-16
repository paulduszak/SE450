package com.duszak.facility.schedule;

public interface Schedule {
	
	public int getEndDay(int orderTime, int requestedQuantity);
	public float scheduleOrder(int orderTime, int requestedQuantity);
}
