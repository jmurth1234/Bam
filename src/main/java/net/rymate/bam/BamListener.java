package net.rymate.bam;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rymate
 */
public class BamListener implements Listener {
    private final Bam plugin;

    public BamListener(Bam aThis) {
        this.plugin = aThis;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public boolean onTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer(); 
        if (plugin.bammedPlayers.containsKey(p.getName())) {
            e.setCancelled(true);
            p.sendMessage("Tryin' to teleport are we? Well, I say NOPE");
        }
        return false;
    }
}
