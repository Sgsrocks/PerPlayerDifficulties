package com.jishunamatata.perplayerdifficulty.gui;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;

public class CustomInventoryManager implements Listener {

	private static HashMap<UUID, InventoryContext> inventoryMap = new HashMap<>();

	public static void openGui(HumanEntity player, CustomInventory inventory) {
		inventoryMap.put(player.getUniqueId(), new InventoryContext(inventory, inventory.open(player)));
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getClickedInventory() == null)
			return;

		UUID uuid = event.getWhoClicked().getUniqueId();

		InventoryContext context = inventoryMap.get(uuid);
		if (context == null)
			return;
		
		InventoryView view = context.getView();
		CustomInventory inventory = context.getInventory();

		if (view != null && view == event.getView() && inventory != null) {
			inventory.consumeClickEvent(event);
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onInventoryClose(InventoryCloseEvent event) {
		if (event.getInventory() == null)
			return;

		UUID uuid = event.getPlayer().getUniqueId();

		InventoryContext context = inventoryMap.get(uuid);
		if (context == null)
			return;
		
		InventoryView view = context.getView();
		CustomInventory inventory = context.getInventory();

		if (view != null && view == event.getView() && inventory != null) {
			inventory.consumeCloseEvent(event);

			inventoryMap.remove(uuid);
		}
	}

}
