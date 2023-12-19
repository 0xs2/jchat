package dev.oxs.jchat.commands;

import dev.oxs.jchat.JChatPlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Date;

public class JChatUnmuteCommand implements CommandExecutor {

    private final Plugin plugin;

    public JChatUnmuteCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    JChatPlayerManager p = new JChatPlayerManager();


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender.hasPermission("jchat.jcunmute") || commandSender.isOp())) {
            commandSender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        } else {

            JSONArray players = p.loadPlayerData();

            if (strings.length != 0) {
                if (!p.isUsernameInArray(players, strings[0])) {
                    commandSender.sendMessage(ChatColor.RED + "No user found.");
                    return true;
                }
                else {

                    JSONObject userObject = p.findUsernameObject(players, strings[0]);

                    String uuid = (String) userObject.get("uuid");
                    boolean muted = (Boolean) userObject.get("isMuted");

                    if(!muted) {
                        commandSender.sendMessage(strings[0] +  " is not currently muted.");
                        return true;
                    }
                    else {
                        p.UpdatePlayerData(players, String.valueOf(uuid), "isMuted", false);
                        commandSender.sendMessage(ChatColor.GREEN + strings[0] +  " was unmuted.");
                        return true;
                    }
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "No user provided.");
                return true;
            }
        }
    }

}