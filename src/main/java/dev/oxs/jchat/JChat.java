package dev.oxs.jchat;

import dev.oxs.jchat.commands.JChatCommand;
import dev.oxs.jchat.commands.JChatMuteCommand;
import dev.oxs.jchat.commands.JChatStatsCommand;
import dev.oxs.jchat.commands.JChatUnmuteCommand;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class JChat extends JavaPlugin implements Listener {
    private Logger log;
    private String pluginName;
    //Basic Plugin Info
    private static JChat plugin;
    private PluginDescriptionFile pdf;

    @Override
    public void onEnable() {

        // create
        Path dir = Paths.get("plugins/JChat");
        try {
            Files.createDirectory(dir);
        } catch (Exception e) {
            System.err.println("Error creating directory: " + e.getMessage());
        }

        plugin = this;
        log = this.getServer().getLogger();
        pdf = this.getDescription();
        pluginName = pdf.getName();

        final JChatListener c = new JChatListener(plugin);
        Bukkit.getPluginManager().registerEvents(c, plugin);

        // commmands
        Bukkit.getPluginCommand("jchat").setExecutor(new JChatCommand(plugin));
        Bukkit.getPluginCommand("jchat-stats").setExecutor(new JChatStatsCommand(plugin));
        Bukkit.getPluginCommand("jchat-jcmute").setExecutor(new JChatMuteCommand(plugin));
        Bukkit.getPluginCommand("jchat-jcunmute").setExecutor(new JChatUnmuteCommand(plugin));

        pluginName = pdf.getName();

        log.info("[" + pluginName + "] Is Loading, Version: " + pdf.getVersion());
    }

    @Override
    public void onDisable() {
        log.info(pluginName + " has been disabled.");
    }
    
}
