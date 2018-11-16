package _112.griefpreventionutilities;

import _112.griefpreventionutilities.Commands.ClaimCleanUp;
import _112.griefpreventionutilities.Commands.DeleteClaims;
import _112.griefpreventionutilities.Commands.SaveClaims;
import _112.griefpreventionutilities.Events.ClaimExpire;
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
    }

    @Override
    public void onDisable() {

    }

    public static GriefPreventionUtilities getPlugin() {
        return plugin;
    }
}
