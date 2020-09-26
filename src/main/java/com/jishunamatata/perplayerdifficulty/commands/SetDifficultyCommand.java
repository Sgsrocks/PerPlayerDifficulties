package com.jishunamatata.perplayerdifficulty.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.jishunamatata.perplayerdifficulty.Difficulty;
import com.jishunamatata.perplayerdifficulty.DifficultyManager;
import com.jishunamatata.perplayerdifficulty.ItemBuilder;
import com.jishunamatata.perplayerdifficulty.PluginStrings;
import com.jishunamatata.perplayerdifficulty.gui.CustomInventory;
import com.jishunamatata.perplayerdifficulty.gui.CustomInventoryManager;

public class SetDifficultyCommand implements CommandExecutor {

	private DifficultyManager difficultyManager;

	public SetDifficultyCommand(DifficultyManager difficultyManager) {
		this.difficultyManager = difficultyManager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}

		if (!sender.hasPermission("perplayerdifficulty.setdifficulty")) {
			sender.sendMessage(
					PluginStrings.ERROR_ICON + ChatColor.RED + "You do not have permission to perform this command.");
			return true;
		}

		Player player = (Player) sender;
		CustomInventory inv = new CustomInventory(null, 27,
				"" + ChatColor.BLACK + ChatColor.BOLD + "Select Difficulty");
		inv.addClickConsumer(this::onInventoryClick);

		ItemStack border = new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).withName(" ").build();

		for (int i = 0; i < 27; i++) {
			if (i < 10 || i > 16) {
				inv.setItem(i, border);
			}
		}
		Difficulty currentDifficulty = difficultyManager.getDifficulty(player);

		int slot = 10;
		for (Difficulty difficulty : difficultyManager.getDifficulties()) {
			inv.setItem(slot++, new ItemBuilder(currentDifficulty == difficulty ? Material.LIME_DYE : Material.GRAY_DYE)
					.withName(difficulty.getDisplayName()).withLore(difficulty.getDescription()).build());
		}

		CustomInventoryManager.openGui(player, inv);

		return true;
	}

	private void onInventoryClick(InventoryClickEvent event) {
		event.setCancelled(true);

		int rawSlot = event.getRawSlot();

		if (event.getCurrentItem() != null && rawSlot >= 10 && rawSlot <= 16) {
			Player player = (Player) event.getWhoClicked();
			difficultyManager.setDifficulty(player, rawSlot - 10);

			Difficulty currentDifficulty = difficultyManager.getDifficulty(player);

			int slot = 10;
			for (Difficulty difficulty : difficultyManager.getDifficulties()) {
				event.getInventory().setItem(slot++,
						new ItemBuilder(currentDifficulty == difficulty ? Material.LIME_DYE : Material.GRAY_DYE)
								.withName(difficulty.getDisplayName()).withLore(difficulty.getDescription()).build());
			}
		}
	}

}
