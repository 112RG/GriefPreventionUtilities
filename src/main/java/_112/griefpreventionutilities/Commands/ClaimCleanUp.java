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

                earliestPermissibleLastLogin.add(Calendar.DATE, -Integer.parseInt(args[0]));
                TaskManager.IMP.async(new BukkitRunnable() {
                    @Override
                    public void run() {
                        String action;
                        if (args.length != 2) action = "check";
                        else action = args[1];

                        gpu.sendMessage(sender, "Finding claims to delete");

                        Integer count = 0;
                        Integer removed = 0;
                        Collection<Claim> toRemove = new ArrayList<>();
                        Collection<Claim> claims = GriefPrevention.instance.dataStore.getClaims();
                        for (Claim claim : claims) {
                            count++;

                            if (Bukkit.getPlayer(claim.ownerID) != null && earliestPermissibleLastLogin.getTime().after(new Date(Bukkit.getPlayer(claim.ownerID).getLastPlayed()))) {
                                removed++;
                                if (!toRemove.contains(claim.ownerID) && action.equals("delete")) {
                                    toRemove.add(claim);
                                    gpu.logMessage(String.format("Claims for %s deleted", claim.ownerID));
                                }
                            } else if (Bukkit.getPlayer(claim.ownerID) == null) {
                                removed++;
                                if (!toRemove.contains(claim.ownerID) && action.equals("delete")) {
                                    toRemove.add(claim);
                                    gpu.logMessage(String.format("Claims for %s deleted", claim.ownerID));
                                }
                            }
                        }

                        switch (action) {
                            case "check":
                                gpu.sendMessage(sender, String.format("Total of %s:%s will be deleted", removed, count));
                                gpu.sendMessage(sender, "Check was specified please check console for list of claims what will be deleted");
                                break;
                            case "delete":
                                toRemove.forEach(remove -> {
                                    GriefPrevention.instance.dataStore.deleteClaim(remove);
                                    gpu.logMessage(String.format("Deleting claim %s", remove.getID()));
                                });
                                break;
                            case "regen":
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
