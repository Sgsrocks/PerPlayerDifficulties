package com.jishunamatata.perplayerdifficulty.gui;

import org.bukkit.inventory.InventoryView;

public class InventoryContext {

	private CustomInventory inventory;
	private InventoryView view;

	public InventoryContext(CustomInventory inventory, InventoryView view) {
		this.inventory = inventory;
		this.view = view;

	}

	public CustomInventory getInventory() {
		return inventory;
	}

	public InventoryView getView() {
		return view;
	}

}
