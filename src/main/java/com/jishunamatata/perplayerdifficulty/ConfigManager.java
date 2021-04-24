package com.jishunamatata.perplayerdifficulty;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.Plugin;

import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.ChatColor;

public class ConfigManager {
	private final Plugin plugin;
	private final DifficultyManager difficultyManager;

	private String inventoryName;

	public ConfigManager(Plugin plugin, DifficultyManager difficultyManager) {
		this.plugin = plugin;
		this.difficultyManager = difficultyManager;

		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdirs();
		}
		loadConfig();
	}

	public void loadConfig() {
		File file = copyResource(this.plugin, "config.yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		this.inventoryName = ChatColor.translateAlternateColorCodes('&',
				config.getString("inventory-name", ChatColor.BLACK.toString() + ChatColor.BOLD + "Select Difficulty"));

		difficultyManager.setDefaultDifficulty(config.getInt("default-difficulty", 1));

		if (config.isConfigurationSection("difficulties")) {
			ConfigurationSection difficultySection = config.getConfigurationSection("difficulties");
			for (String diff : difficultySection.getKeys(false)) {
				Difficulty difficulty = new Difficulty();

				difficulty.setDisplayName(difficultySection.getString(diff + ".display", "Default Name"));
				difficulty.setDescription(difficultySection.getStringList(diff + ".description"));

				difficulty.setPvpMultiplier(difficultySection.getDouble(diff + ".pvp-multiplier", 1d));
				difficulty.setPveMultiplier(difficultySection.getDouble(diff + ".pve-multiplier", 1d));

				difficulty.setLoseHunger(difficultySection.getBoolean(diff + ".lose-hunger", true));

				difficulty.setExpMultiplier(difficultySection.getDouble(diff + ".mob-experience-multiplier", 1d));

				for (DamageCause cause : DamageCause.values()) {
					if (cause == DamageCause.ENTITY_ATTACK)
						continue;

					double multiplier = difficultySection
							.getDouble(diff + "." + cause.name().toLowerCase() + "-multiplier", -1d);

					if (multiplier >= 0d) {
						difficulty.addDamageMultiplier(cause, multiplier);
					}
				}

				difficultyManager.registerDifficulty(difficulty);
			}
		}

	}

	public void reloadConfig() {
		difficultyManager.clearDifficulties();
		difficultyManager.flushCache();
		loadConfig();
	}

	public String getInventoryName() {
		return inventoryName;
	}

	// saveDefaultConfig doesn't copy comments, this will
	private File copyResource(Plugin plugin, String resource) {
		File folder = plugin.getDataFolder();
		File resourceFile = new File(folder, resource);
		if (!resourceFile.exists()) {
			try {
				resourceFile.createNewFile();
				try (InputStream in = plugin.getResource(resource);
						OutputStream out = new FileOutputStream(resourceFile)) {
					ByteStreams.copy(in, out);
				}

			} catch (Exception e) {
				Bukkit.getLogger().severe("Error copying file " + resource);
			}
		}
		return resourceFile;
	}
}
