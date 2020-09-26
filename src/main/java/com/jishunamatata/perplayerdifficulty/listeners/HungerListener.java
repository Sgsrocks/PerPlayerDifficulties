package com.jishunamatata.perplayerdifficulty.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import com.jishunamatata.perplayerdifficulty.Difficulty;
import com.jishunamatata.perplayerdifficulty.DifficultyManager;

public class HungerListener implements Listener {

	private DifficultyManager difficultyManager;

	public HungerListener(DifficultyManager difficultyManager) {
		this.difficultyManager = difficultyManager;
	}

	@EventHandler(ignoreCancelled = true)
	public void onFoodChange(FoodLevelChangeEvent event) {
		Player player = (Player) event.getEntity();
		if (player.getFoodLevel() < event.getFoodLevel())
			return;

		Difficulty difficulty = difficultyManager.getDifficulty(player);

		if (!difficulty.shouldLoseHunger()) {
			event.setCancelled(true);
		}
	}

}
