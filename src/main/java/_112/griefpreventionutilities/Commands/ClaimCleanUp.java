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

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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

                        long timeNow = Instant.now().toEpochMilli();
                        long minusTime = ((long) Integer.parseInt(args[0]));
                        minusTime *= 86400000; //days -> milliseconds
                        long borderTime = timeNow - minusTime;
                        for (Claim claim : claims) {
                            count++;
                            try {
                                Bukkit.getOfflinePlayer(claim.ownerID);
                            } catch (IllegalArgumentException e) {
                                if (!claim.isAdminClaim()) {
                                    gpu.sendMessage(sender, String.format("Invalid owner ID - coordinates %s", claim.getLesserBoundaryCorner().toString()));
                                    removed++;
                                    if (action.equals("delete")) {
                                        toRemove.add(claim);
                                    }
                                }
                                continue;
                            }
                            if (!Bukkit.getOfflinePlayer(claim.ownerID).hasPlayedBefore()) {
                                removed++;
                                if (action.equals("delete")) {
                                    toRemove.add(claim);
                                }
                            } else if (Bukkit.getOfflinePlayer(claim.ownerID).getLastPlayed() < borderTime) {
                                removed++;
                                if (action.equals("delete")) {
                                    toRemove.add(claim);
                                }
                            }
                        }
                        switch (action) {
                            case "check":
                                gpu.sendMessage(sender, String.format("Total of %s:%s will be deleted", removed, count));
                                break;
                            case "delete":
                                if (toRemove.size() != 0) {
                                    toRemove.forEach(remove -> {
                                        try {
                                            Thread.sleep(2);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        GriefPrevention.instance.dataStore.deleteClaim(remove);
                                    });
                                } else {
                                    gpu.sendMessage(sender, "No claims to delete");
                                }
                                gpu.sendMessage(sender, "Deleted " + removed.toString() + " claims.");

                                break;
                            case "regen":
                                break;
                            default:
                                break;
                        }
                    }
                });
            } else {
                gpu.sendMessage(sender, "Please specify number of days /claimcleanup <number of days> [check/delete]");
            }
        }
        return true;

    }

}
