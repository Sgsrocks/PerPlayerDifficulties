package com.jishunamatata.perplayerdifficulty.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.jishunamatata.perplayerdifficulty.Difficulty;
import com.jishunamatata.perplayerdifficulty.DifficultyManager;

public class DamageListener implements Listener {

	private DifficultyManager difficultyManager;

	public DamageListener(DifficultyManager difficultyManager) {
		this.difficultyManager = difficultyManager;
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerDamage(EntityDamageEvent event) {
		if (event.getEntityType() != EntityType.PLAYER || event.getCause() == DamageCause.ENTITY_ATTACK)
			return;

		Player player = (Player) event.getEntity();
		Difficulty difficulty = difficultyManager.getDifficulty(player);

		event.setDamage(event.getDamage() * difficulty.getDamageMultiplier(event.getCause()));
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getEntityType() != EntityType.PLAYER || event.getCause() != DamageCause.ENTITY_ATTACK)
			return;

		Player player = (Player) event.getEntity();
		Difficulty difficulty = difficultyManager.getDifficulty(player);

		if (event.getDamager().getType() == EntityType.PLAYER) {
			event.setDamage(event.getDamage() * difficulty.getPvpMultiplier());
		} else {
			event.setDamage(event.getDamage() * difficulty.getPveMultiplier());	
		}
	}
}
