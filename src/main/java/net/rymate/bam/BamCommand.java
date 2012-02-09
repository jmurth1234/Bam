
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
class BamCommand implements CommandExecutor {

    private final Bam plugin;

    public BamCommand(Bam aThis) {
        this.plugin = aThis;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[0] == null) {
            sender.sendMessage("You need to specify a player!");
            return false;
        }
        int i;

        if (sender.hasPermission("Bam.bam")) {
            World nether = plugin.getServer().getWorlds().get(1);
            Player other = plugin.getServer().getPlayer(args[0]);

            if (other == null) {
                sender.sendMessage(ChatColor.RED + "You do know that " + args[0] + " is not online, right? ");
                return false;
            }

            if (other.hasPermission("Bam.exempt")) {
                sender.sendMessage(ChatColor.RED + "A protective veil stops you bamming " + args[0] + ".");
                return false;
            }

            other.teleport(new Location(nether, 0.0, 130.0, 0.0));
            System.out.println("Ok, we're starting at BamCommand. Lets see what the player is:" + other);
            boolean bammed = plugin.bamPlayer(other);
            if (bammed) {
                if (args[1] == null) {
                    plugin.getServer().broadcastMessage(other.getName() + "was bammed to the nether! He must've been naughty :D");
                } else {
                    StringBuilder msg = new StringBuilder();
                    for (i = 1; i < args.length; i++) {
                        msg.append(args[i]);
                        msg.append(" ");
                    }
                    plugin.getServer().broadcastMessage(other.getName() + "was bammed to the nether for: " + msg);
                }
            } else {
                sender.sendMessage(ChatColor.RED + "This player is already bammed!");
            }

            return true;
        } else {
            sender.sendMessage("Y U TRY TO USE COMMAND");
            return true;
        }
    }
}
