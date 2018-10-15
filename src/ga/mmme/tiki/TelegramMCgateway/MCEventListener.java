package ga.mmme.tiki.TelegramMCgateway;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ga.mmme.tiki.TelegramMCgateway.Telegram.Bot;

public class MCEventListener implements Listener {
	Bot telegrambot;
	FileConfiguration config;
	public MCEventListener(Bot telegrambot, FileConfiguration config) {
		this.telegrambot = telegrambot;
		this.config = config;
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		try {
			telegrambot.sendMessage(String.format(config.getString("tg-messages.connected.text"), e.getPlayer().getName()), config.getInt("tg-messages.connected.style"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		try {
			telegrambot.sendMessage(String.format(config.getString("tg-messages.death.text"), e.getDeathMessage()), config.getInt("tg-messages.death.style"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		try {
			telegrambot.sendMessage(String.format(config.getString("tg-messages.disconnected.text"), e.getPlayer().getName()), config.getInt("tg-messages.disconnected.style"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(!e.isCancelled()) {
			try {
				telegrambot.sendMessage(String.format(config.getString("tg-messages.chat.text"), e.getPlayer().getName(), e.getMessage()),config.getInt("tg-messages.chat.style"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
