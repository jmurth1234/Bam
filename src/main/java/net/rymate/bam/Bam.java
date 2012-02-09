package net.rymate.bam;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Bam extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    public static String name = "Bam";
    public static String version = "1.0";
    File f;
    private BamListener listener = new BamListener(this);
    public Map<String, String[]> bammedPlayers = new HashMap<String, String[]>();
    public static Permission permission;
    private String bamGroup = "jailed";

    @Override
    public void onEnable() {
        setupConfig();
        this.getServer().getPluginManager().registerEvents(this.listener, this);
        getCommand("bam").setExecutor(new BamCommand(this));
        getCommand("unbam").setExecutor(new UnbamCommand(this));
        f = new File(this.getDataFolder() + File.separator + "bammedPlayers.bin");
        if (f.exists()) {
            try {
                bammedPlayers = (Map<String, String[]>) SLAPI.load("bammedPlayers.bin");
                log.info(name + " version " + version + " has loaded the bammed players! Its a miracle!!!");
            } catch (Exception ex) {
                Logger.getLogger(Bam.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.bamGroup = getConfig().getString("group-to-set", this.bamGroup);
        Boolean perms = setupPermissions();
        if (perms) {
            log.info(name + " version " + version + " has found permissions! We'll work  properly now!");
        }
    }

    @Override
    public void onDisable() {
        log.info("Why did you disable " + name + " version " + version + "? :(");
        try {
            SLAPI.save(bammedPlayers, f.getAbsolutePath());
        } catch (Exception ex) {
            log.warning(ex.toString());
        }
    }

    private Boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    public boolean bamPlayer(Player player) {
        if (!bammedPlayers.containsKey(player.getName())) {
            String[] groups = permission.getPlayerGroups(player);
            bammedPlayers.put(player.getName(), groups);
            for (int i = 0; i < groups.length; i++) {
                permission.playerRemoveGroup(player, groups[i]);
            }
            permission.playerAddGroup(player, bamGroup);
            try {
                SLAPI.save(bammedPlayers, f.getAbsolutePath());
            } catch (Exception ex) {
                log.warning(ex.toString());
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean unbamPlayer(Player other) {
        if (bammedPlayers.containsKey(other.getName())) {
            String[] groups = bammedPlayers.get(other.getName());
            bammedPlayers.remove(other.getName());
            for (int i = 0; i < groups.length; i++) {
                permission.playerAddGroup(other, groups[i]);
            }
            permission.playerRemoveGroup(other, bamGroup);
            try {
                SLAPI.save(bammedPlayers, f.getAbsolutePath());
            } catch (Exception ex) {
                log.warning(ex.toString());
            }
            return true;
        } else {
            return false;
        }
    }

    private void setupConfig() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }
}
