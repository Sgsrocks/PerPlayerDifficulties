package com.jishunamatata.perplayerdifficulty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

public class DifficultyManager {

	private final NamespacedKey difficultyKey;

	private final Map<UUID, Difficulty> playerCache = new HashMap<>();
	private final List<Difficulty> difficulties = new ArrayList<>();
	private int defaultDifficulty = 0;

	public DifficultyManager(Plugin plugin) {
		this.difficultyKey = new NamespacedKey(plugin, "player_difficulty");
	}

	public void registerDifficulty(Difficulty difficulty) {
		difficulties.add(difficulty);
	}

	public void setDefaultDifficulty(int defaultDifficulty) {
		this.defaultDifficulty = defaultDifficulty;
	}

	public Difficulty getDifficulty(Player player) {
		PersistentDataContainer container = player.getPersistentDataContainer();
		UUID uuid = player.getUniqueId();

		Difficulty difficulty = playerCache.get(uuid);

		if (difficulty == null) {
			int index = container.has(difficultyKey, PersistentDataType.BYTE)
					? container.get(difficultyKey, PersistentDataType.BYTE)
					: defaultDifficulty;

			difficulty = difficulties.get(index);
			playerCache.put(uuid, difficulty);
		}

		return difficulty;
	}

	public void setDifficulty(Player player, int value) {
		if (value >= difficulties.size()) {
			throw new IllegalArgumentException("A difficulty with value " + value + " is not registered");
		}

		Difficulty difficulty = difficulties.get(value);

		player.getPersistentDataContainer().set(difficultyKey, PersistentDataType.BYTE, (byte) value);
		playerCache.put(player.getUniqueId(), difficulty);

		player.sendMessage(PluginStrings.SUCCESS_ICON + ChatColor.GREEN + "Your personal difficulty has been set to "
				+ difficulty.getDisplayName());
	}

	public List<Difficulty> getDifficulties() {
		return difficulties;
	}

	public void clearDifficulties() {
		this.difficulties.clear();
	}

}
