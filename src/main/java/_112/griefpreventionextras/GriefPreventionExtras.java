package _112.griefpreventionextras;

import _112.griefpreventionextras.Commands.ClaimCleanUp;
import _112.griefpreventionextras.Commands.DeleteClaims;
import _112.griefpreventionextras.Commands.SaveClaims;
import _112.griefpreventionextras.Events.ClaimExpire;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.java.JavaPlugin;

public final class GriefPreventionExtras extends JavaPlugin {
    private static GriefPreventionExtras plugin;

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

    public static GriefPreventionExtras getPlugin() {
        return plugin;
    }
}
