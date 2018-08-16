package com.duszak.order;

import java.util.Map;

public interface Order {
	public String getId();
	public int getTime();
	public String getDestination();
	public Map<String, Integer> getItemsCopy();
}
