package _112.griefpreventionutilities.Commands;

import _112.griefpreventionutilities.GriefPreventionUtilities;
import _112.griefpreventionutilities.Utils.LocationHelper;
import com.boydti.fawe.object.FawePlayer;
import com.boydti.fawe.util.TaskManager;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CountClaims implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player) {
            GriefPreventionUtilities gpu = GriefPreventionUtilities.getPlugin();
            FawePlayer fawePlayer = FawePlayer.wrap(sender);
            if (fawePlayer.getSelection() != null) {
                LocationHelper locationHelper = new LocationHelper(fawePlayer.getSelection().getMinimumPoint(), fawePlayer.getSelection().getMaximumPoint(), ((Player) sender).getWorld());
                TaskManager.IMP.async(new BukkitRunnable() {
                    @Override
                    public void run() {
                        int count = 0;
                        for (Claim claim : GriefPrevention.instance.dataStore.getClaims()) {
                            if (locationHelper.locationIsInRegion(claim.getLesserBoundaryCorner()) && locationHelper.locationIsInRegion(claim.getGreaterBoundaryCorner())) {
                                count++;
                            }
                        }

                        gpu.sendMessage(sender, String.format("There is &a%s&r claims in your current selection", count));
                    }
                });

            } else {
                gpu.sendMessage(sender, "Please make a WE selection");
            }
        }
        return true;
    }
}
