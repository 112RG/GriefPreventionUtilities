package _112.griefpreventionutilities;

import _112.griefpreventionutilities.Commands.ClaimCleanUp;
import _112.griefpreventionutilities.Commands.CountClaims;
import _112.griefpreventionutilities.Commands.DeleteClaims;
import _112.griefpreventionutilities.Commands.SaveClaims;
import _112.griefpreventionutilities.Events.ClaimExpire;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class GriefPreventionUtilities extends JavaPlugin {
    private static GriefPreventionUtilities plugin;

    @Override
    public void onEnable() {
        plugin = this;
        getServer().getPluginManager().registerEvents(new ClaimExpire(), this);
        this.getCommand("claimcleanup").setExecutor(new ClaimCleanUp());
        this.getCommand("saveclaims").setExecutor(new SaveClaims());
        this.getCommand("edeleteclaims").setExecutor(new DeleteClaims());
        this.getCommand("countclaims").setExecutor(new CountClaims());
    }

    @Override
    public void onDisable() {
        plugin = null;
    }

     public void sendMessage(CommandSender p, String message){
        message = ChatColor.translateAlternateColorCodes('&', "&C&LGPU &F&L>&r " + message);
        p.sendMessage(message);
     }
    public static GriefPreventionUtilities getPlugin() {
        return plugin;
    }
}
