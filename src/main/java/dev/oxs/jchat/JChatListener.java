package dev.oxs.jchat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Date;

public class JChatListener implements Listener {

    private JChat plugin;

    public JChatListener(JChat plugin) {
        this.plugin = plugin;
    }

    JChatManager chat = new JChatManager();
    JChatPlayerManager p = new JChatPlayerManager();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        JSONArray players = p.loadPlayerData();

        if(!p.isUUIDInArray(players, String.valueOf(player.getUniqueId()))) {
            p.InsertPlayerEntry(player.getName(), String.valueOf(player.getUniqueId()));
        }
        else {
            p.UpdatePlayerData(players, String.valueOf(player.getUniqueId()), "lastSeen", new Date().getTime());
            p.UpdatePlayerData(players, String.valueOf(player.getUniqueId()), "user", player.getName());
        }
    }

    @EventHandler
        public void onBlockPlace(BlockPlaceEvent event) {

            if (event.getBlockPlaced().getType() == Material.SIGN || event.getBlockPlaced().getType() == Material.SIGN_POST || event.getBlockPlaced().getType() == Material.WALL_SIGN) {

                JSONArray players = p.loadPlayerData();
                Player player = event.getPlayer();

                JSONObject userObject = p.findUUIDObject(players, String.valueOf(player.getUniqueId()));
                if (userObject != null) {
                    boolean isMuted = (boolean) userObject.get("isMuted");

                    if (isMuted) {
                        event.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "You are not allowed to place signs when you are muted.");
                    }
                }
            }
        }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        JSONArray players = p.loadPlayerData();

        JSONObject userObject = p.findUUIDObject(players, String.valueOf(player.getUniqueId()));
        if (userObject != null) {
            boolean isMuted = (boolean) userObject.get("isMuted");

            if (isMuted) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You are muted.");
            }
            else {
                chat.InsertChatEntry(player.getName(), String.valueOf(player.getUniqueId()), message, false);

            }
        }

    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage();

        chat.InsertChatEntry(player.getName(), String.valueOf(player.getUniqueId()), command.substring(1), true);
    }

}
