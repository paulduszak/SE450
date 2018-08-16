package com.duszak.item;

import java.util.HashMap;
import java.util.Map;

import com.duszak.util.loader.Loader;
import com.duszak.util.loader.LoaderFactory;

public class ItemCatalog {
	
	private static ItemCatalog instance;
	private static Map<String, Item> items;
	
	private ItemCatalog() { 
		if (items == null) {
			Loader loader = LoaderFactory.createLoader("item");
			items = new HashMap<String, Item>();
			
			for (Object i : loader.load("src/data/items.xml")) {
				Item item = (Item)i;
				items.put(item.getItemId(), item);
			}
		}
	}
	
	public static ItemCatalog getInstance() {
		if (instance == null)
			instance = new ItemCatalog();
		
		return instance;
	}
	
	public static float getItemCost(String itemId) {
		return items.get(itemId).getCost();
	}
	
	private static Map<String, Item> getItems() {
		return items;
	}

	public static void printItems() {
		System.out.println(String.format("%10s   %s", "Item ID", "Cost"));
		for (Item i : getItems().values()) {
			System.out.println(i);
		}
		System.out.println("\n");
	}
	
	public static boolean itemExists(String itemId) {
		
		Item i = getItems().get(itemId);
		
		if(i != null)
			return true;
		else
			return false;
	}
	
}
