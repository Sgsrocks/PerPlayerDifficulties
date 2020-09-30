package com.jishunamatata.perplayerdifficulty.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.jishunamatata.perplayerdifficulty.Difficulty;
import com.jishunamatata.perplayerdifficulty.DifficultyManager;

public class MobListener implements Listener {

	private DifficultyManager difficultyManager;

	public MobListener(DifficultyManager difficultyManager) {
		this.difficultyManager = difficultyManager;
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onMobDeath(EntityDeathEvent event) {
		if (event.getEntityType() == EntityType.PLAYER || event.getDroppedExp() <= 0) {
			return;
		}

		Player player = event.getEntity().getKiller();

		if (player != null) {
			Difficulty difficulty = difficultyManager.getDifficulty(player);
			event.setDroppedExp((int) Math.round(event.getDroppedExp() * difficulty.getExpMultiplier()));
		}
	}

}
