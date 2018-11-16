package _112.griefpreventionutilities.Commands;

import _112.griefpreventionutilities.GriefPreventionUtilities;
import com.boydti.fawe.util.TaskManager;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.UUID;
import java.util.Date;

public class ClaimCleanUp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            GriefPreventionUtilities gpu = GriefPreventionUtilities.getPlugin();
            if (args.length >= 1 && args[0] != null) {
                try {
                    Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    gpu.sendMessage(sender, String.format("&c%s&r is not a integer", args[0]));
                    return true;
                }


                Calendar earliestPermissibleLastLogin = Calendar.getInstance();
                earliestPermissibleLastLogin.add(Calendar.DATE, -GriefPrevention.instance.config_claims_expirationDays);
                TaskManager.IMP.async(new BukkitRunnable() {
                    @Override
                    public void run() {
                        int count = 0;
                        Collection<UUID> toRemove = new ArrayList<>();

                        for (Claim claim : GriefPrevention.instance.dataStore.getClaims()) {
                            if (Bukkit.getPlayer(claim.ownerID) != null && earliestPermissibleLastLogin.getTime().after(new Date(Bukkit.getPlayer(claim.ownerID).getLastPlayed()))) {
                                count = count + 1;
                                if (!toRemove.contains(claim.ownerID)) {
                                    toRemove.add(claim.ownerID);
                                    Bukkit.getLogger().info(String.format("Claims for %s will be deleted", claim.ownerID));
                                }
                                continue;
                            } else {
                                count = count + 1;
                                if (!toRemove.contains(claim.ownerID)) {
                                    toRemove.add(claim.ownerID);
                                    Bukkit.getLogger().info(String.format("Claims for %s will be deleted", claim.ownerID));
                                }
                            }
                        }
                        String action;
                        if (args.length != 2) action = "check";
                        else action = args[1];
                        switch (action) {
                            case "check":
                                gpu.sendMessage(sender, "Check was specified please check console for list of claims what will be deleted");
                                break;
                            case "regen":
                                break;
                            case "delete":
                                if (toRemove.size() == 0) {
                                    gpu.sendMessage(sender, "No claims to remove");
                                } else {
                                    toRemove.forEach(uuid -> {
                                        GriefPrevention.instance.dataStore.deleteClaimsForPlayer(uuid, false);
                                        Bukkit.getLogger().info(String.format("Deleted claims for %s", uuid));

                                    });
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
            } else {
                gpu.sendMessage(sender, "Please specify number of days /claimcleanup <number of days>");
            }
        }
        return true;

    }

}
