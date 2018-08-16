package com.duszak.facility.schedule;

import java.util.HashMap;
import java.util.Map;

public class ScheduleImpl implements Schedule {
	private Map<Integer, Integer> schedule;
	
	public ScheduleImpl(int rateIn) {
		schedule = new HashMap<Integer, Integer>();
		
		for (int i = 1; i < 180; i++)
			schedule.put(i, rateIn);
	}
	
	@Override
	public int getEndDay(int orderTime, int requestedQuantity) {
		
		while (requestedQuantity > 0) {
			requestedQuantity -= schedule.get(orderTime);
			orderTime += 1;
		}
		
		return orderTime;
	}
	
	@Override
	public float scheduleOrder(int orderTime, int requestedQuantity) {
		float days = (float) 0.0;

		while (requestedQuantity > 0) {
			if (requestedQuantity >= schedule.get(orderTime)) {
				requestedQuantity -= schedule.get(orderTime);
				schedule.put(orderTime, 0);
				days += 1;
			} else {
				int remainingAvailability = schedule.get(orderTime) - requestedQuantity;
				requestedQuantity = 0;
				schedule.put(orderTime, remainingAvailability);
				days += requestedQuantity / schedule.get(orderTime);
			}
			orderTime += 1;
		}
		
		return days;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		
		s.append("Schedule: ");
		s.append(System.getProperty("line.separator"));
		
		s.append(String.format("%-13s", "Day:"));
		for (int day : schedule.keySet())
			if (day <= 20)
				s.append(String.format("%-3s", Integer.toString(day)));
		
		s.append(System.getProperty("line.separator"));
		
		s.append(String.format("%-13s", "Available:"));
		for (int day : schedule.keySet())
			if (day <= 20)
				s.append(String.format("%-3s", Integer.toString(schedule.get(day))));
		
		s.append(System.getProperty("line.separator"));
		return s.toString();
	}
}
