package com.jishunamatata.perplayerdifficulty;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import net.md_5.bungee.api.ChatColor;

public class Difficulty {

	private String display;
	private List<String> description = new ArrayList<String>();
	private Map<DamageCause, Double> multiplierMap = new EnumMap<DamageCause, Double>(DamageCause.class);
	private double pvpMultiplier, pveMultiplier;
	private boolean loseHunger;

	public String getDisplayName() {
		return display;
	}

	public void setDisplayName(String display) {
		this.display = ChatColor.translateAlternateColorCodes('&', display);
	}

	public List<String> getDescription() {
		return description;
	}

	public void setDescription(List<String> description) {
		description.forEach((String line) -> this.description.add(ChatColor.translateAlternateColorCodes('&', line)));
	}

	public void addDamageMultiplier(DamageCause cause, double multiplier) {
		this.multiplierMap.put(cause, multiplier);
	}

	public double getDamageMultiplier(DamageCause cause) {
		Double multiplier = this.multiplierMap.get(cause);

		return multiplier == null ? 1d : multiplier;
	}

	public double getPvpMultiplier() {
		return pvpMultiplier;
	}

	public void setPvpMultiplier(double pvpMultiplier) {
		this.pvpMultiplier = pvpMultiplier;
	}

	public double getPveMultiplier() {
		return pveMultiplier;
	}

	public void setPveMultiplier(double pveMultiplier) {
		this.pveMultiplier = pveMultiplier;
	}

	public boolean shouldLoseHunger() {
		return loseHunger;
	}

	public void setLoseHunger(boolean loseHunger) {
		this.loseHunger = loseHunger;
	}

}
