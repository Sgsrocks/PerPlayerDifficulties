package com.jishunamatata.perplayerdifficulty;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.jishunamatata.perplayerdifficulty.commands.PlayerDifficultyCommand;
import com.jishunamatata.perplayerdifficulty.commands.SetDifficultyCommand;
import com.jishunamatata.perplayerdifficulty.gui.CustomInventoryManager;
import com.jishunamatata.perplayerdifficulty.listeners.DamageListener;
import com.jishunamatata.perplayerdifficulty.listeners.HungerListener;
import com.jishunamatata.perplayerdifficulty.listeners.MobListener;

public class PerPlayerDifficulty extends JavaPlugin {

	private DifficultyManager difficultyManager = new DifficultyManager(this);
	private ConfigManager configManager = new ConfigManager(this, difficultyManager);

	public void onEnable() {
		PluginManager pm = Bukkit.getPluginManager();

		pm.registerEvents(new CustomInventoryManager(), this);
		pm.registerEvents(new DamageListener(difficultyManager), this);
		pm.registerEvents(new HungerListener(difficultyManager), this);
		pm.registerEvents(new MobListener(difficultyManager), this);

		getCommand("setdifficulty").setExecutor(new SetDifficultyCommand(configManager, difficultyManager));
		getCommand("playerdifficulties").setExecutor(new PlayerDifficultyCommand(configManager));
	}

}
