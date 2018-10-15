package ga.mmme.tiki.TelegramMCgateway;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import ga.mmme.tiki.TelegramMCgateway.Telegram.Bot;
import ga.mmme.tiki.TelegramMCgateway.Telegram.ParseModes;
import ga.mmme.tiki.TelegramMCgateway.Telegram.Update;
import ga.mmme.tiki.TelegramMCgateway.Telegram.User;

public class Main extends JavaPlugin {
	MCEventListener mclistener;
	Bot tgbot;
	BukkitTask task;
	@Override
    public void onEnable() {
		this.saveDefaultConfig();
		tgbot = new Bot(this.getConfig().getString("BotToken"),0,this.getConfig().getLong("ChatID"),this);
		mclistener = new MCEventListener(tgbot,this.getConfig());
		getServer().getPluginManager().registerEvents(mclistener, this);
		task = getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
			@Override
			public void run() {
				try {
					Update[] updates = tgbot.getUpdates();
					for (Update update : updates) {
						String msg;
						User user = update.getMessage().getFrom();
						if(update.getMessage().getText().startsWith("/online"))
						{
							if(!Bukkit.getServer().getOnlinePlayers().isEmpty()) {
								msg = "Jugadores conectados:\n";
								Iterator<? extends Player> player = Bukkit.getServer().getOnlinePlayers().iterator();
								while (player.hasNext()) {
									msg+=player.next().getName()+"\n";
								}
								tgbot.sendMessage(msg, ParseModes.NONE);
							}
							else {
								tgbot.sendMessage("No hay jugadores conectados.", ParseModes.NONE);
							}
						}
						else {
							if(user.getUsername()!=null) {
								msg = String.format(Main.this.getConfig().getString("messages.telegram-username"), Main.this.getConfig().getString("messages.mc-prefix"), user.getUsername() ,update.getMessage().getText());
							}
							else {
								if(user.getLast_name()!=null) {
									msg = String.format(Main.this.getConfig().getString("messages.telegram-name-lastname"), Main.this.getConfig().getString("messages.mc-prefix"), user.getFirst_name(), user.getLast_name(),update.getMessage().getText());
								}
								else msg = String.format(Main.this.getConfig().getString("messages.telegram-name"), Main.this.getConfig().getString("messages.mc-prefix"), user.getFirst_name() ,update.getMessage().getText());
							}
							Bukkit.broadcastMessage(msg);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 60, 60);
    }
    @Override
    public void onDisable() {
    	HandlerList.unregisterAll(mclistener);
    	getServer().getScheduler().cancelTask(task.getTaskId());
    	
    }
}
