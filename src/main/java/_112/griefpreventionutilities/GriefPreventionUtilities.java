package _112.griefpreventionutilities;

import _112.griefpreventionutilities.Commands.*;
import _112.griefpreventionutilities.Events.ClaimExpire;
import _112.griefpreventionutilities.Utils.PrivateInventory;
import com.boydti.fawe.util.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class GriefPreventionUtilities extends JavaPlugin {
    private static GriefPreventionUtilities plugin;

    @Override
    public void onEnable() {
        plugin = this;
        getServer().getPluginManager().registerEvents(new ClaimExpire(), this);
        getServer().getPluginManager().registerEvents(PrivateInventory.getListener(), this);

        this.getCommand("claimcleanup").setExecutor(new ClaimCleanUp());
        this.getCommand("saveclaims").setExecutor(new SaveClaims());
        this.getCommand("edeleteclaims").setExecutor(new DeleteClaims());
        this.getCommand("countclaims").setExecutor(new CountClaims());
        this.getCommand("deleteallunclaimed").setExecutor(new Deleteunclaimed());
        this.getCommand("eclaims").setExecutor(new EClaims());
        plugin.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        plugin = null;
    }

    public void sendMessage(CommandSender p, String message) {
        TaskManager.IMP.async(new BukkitRunnable() {
            @Override
            public void run() {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&C&LGPU &F&L>&r " + message));

            }
        });
    }

    public void logMessage(String message){
        TaskManager.IMP.async(new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&C&LGPU&r " + message));
            }
        });
    }

    public static GriefPreventionUtilities getPlugin() {
        return plugin;
    }
}
