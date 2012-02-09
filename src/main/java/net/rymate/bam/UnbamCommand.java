/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rymate.bam;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author rymate
 */
class UnbamCommand implements CommandExecutor {

    private final Bam plugin;

    public UnbamCommand(Bam aThis) {
        this.plugin = aThis;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[0] == null) {
            sender.sendMessage("You need to specify a player!");
            return false;
        }

        if (sender.hasPermission("Bam.free")) {
            World world = plugin.getServer().getWorlds().get(0);
            Player other = plugin.getServer().getPlayer(args[0]);

            if (other == null) {
                sender.sendMessage(ChatColor.RED + "You do know that " + args[0] + " is not online, right? ");
            }

            boolean bammed = plugin.unbamPlayer(other);
            if (bammed) {
                plugin.getServer().broadcastMessage(other.getName() + " is now free! Let's hope he learn't his lesson eh? ;)");
                other.teleport(world.getSpawnLocation());
            } else {
                sender.sendMessage(ChatColor.RED + "This player isn't bammed!");
            }

            return true;
        } else {
            sender.sendMessage("Y U TRY TO USE COMMAND");
            return true;
        }
    }
}
