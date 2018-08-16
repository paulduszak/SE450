package com.duszak.facility.inventory;

import com.duszak.exception.InvalidDataException;
import com.duszak.exception.NullParameterException;
import com.duszak.item.ItemCatalog;

public class Inventory {
	private String itemId;
	private int quantity;
	
	public Inventory(String itemIdIn, int quantityIn) throws NullParameterException, InvalidDataException {
		setItemId(itemIdIn);
		setQuantity(quantityIn);
	}
	
	public int getQuantity() {
		return this.quantity;
	}
	
	public String getItemId() {
		return this.itemId;
	}
	
	public void reduceQuantity(int count) throws InvalidDataException {
		setQuantity(getQuantity() - count);
	}
	
	private void setItemId(String itemIdIn) throws NullParameterException, InvalidDataException {
		ItemCatalog.getInstance();
		
		if (itemIdIn == null)
			throw new NullParameterException("Inventory 'itemId' cannot be null.");
		if (!ItemCatalog.itemExists(itemIdIn))
			throw new InvalidDataException("An item which does not exist in the item Catalog has been specified: " + itemIdIn);
			
		itemId = itemIdIn;
	}
	
	private void setQuantity(int quantityIn) throws InvalidDataException {
		if (quantityIn < 0)
			throw new InvalidDataException("Inventory 'quantity' cannot be negative.");

		quantity = quantityIn;
	}	
}
