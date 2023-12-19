package dev.oxs.jchat.commands;

import dev.oxs.jchat.JChatManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JChatStatsCommand implements CommandExecutor {

    private final Plugin plugin;
    private final JChatManager chat = new JChatManager();

    public JChatStatsCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender.hasPermission("jchat.jcstats") || commandSender.isOp())) {
            commandSender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        } else {

            JSONArray msg  = chat.loadChatLog();

            int m = 0;
            int c = 0;

            for (Object obj : msg) {
                if (obj instanceof JSONObject) {
                    JSONObject jsonObj = (JSONObject) obj;
                    if (Boolean.TRUE.equals(jsonObj.get("isCommand"))) {
                        c++;
                    }
                    if (Boolean.FALSE.equals(jsonObj.get("isCommand"))) {
                        m++;
                    }

                }
            }

            commandSender.sendMessage( "Messages logged : " + ChatColor.BLUE + m);
            commandSender.sendMessage("Commands logged : " + ChatColor.BLUE + c);
            commandSender.sendMessage("Total : " + ChatColor.BLUE + msg.size());
            return true;
        }
    }

}
