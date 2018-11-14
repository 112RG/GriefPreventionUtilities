package _112.griefpreventionextras;

import _112.griefpreventionextras.Commands.ClaimCleanUp;
import _112.griefpreventionextras.Commands.SaveClaims;
import _112.griefpreventionextras.Events.ClaimExpire;
import org.bukkit.plugin.java.JavaPlugin;

public final class GriefPreventionExtras extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ClaimExpire(), this);
        this.getCommand("claimcleanup").setExecutor(new ClaimCleanUp());
        this.getCommand("saveclaims").setExecutor(new SaveClaims());
    }

    @Override
    public void onDisable() {

    }
}
