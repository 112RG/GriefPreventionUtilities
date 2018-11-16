package _112.griefpreventionutilities.Commands;

import _112.griefpreventionutilities.GriefPreventionUtilities;
import _112.griefpreventionutilities.Util.Region;
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

import java.util.ArrayList;
import java.util.Collection;

public class DeleteClaims implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(sender instanceof Player) {
            GriefPreventionUtilities gpu = GriefPreventionUtilities.getPlugin();
            FawePlayer fawePlayer = FawePlayer.wrap(sender);
            if (fawePlayer.getSelection() != null) {
                World world = Bukkit.getWorld(fawePlayer.getWorld().getName());
                Region region = new Region(fawePlayer.getSelection().getMinimumPoint(), fawePlayer.getSelection().getMaximumPoint(), world);
                TaskManager.IMP.async(new BukkitRunnable() {
                    @Override
                    public void run() {
                        Collection<Claim> toRemove = new ArrayList<>();
                        for (Claim claim : GriefPrevention.instance.dataStore.getClaims()) {
                            if (region.locationIsInRegion(claim.getLesserBoundaryCorner()) || region.locationIsInRegion(claim.getGreaterBoundaryCorner())) {
                                toRemove.add(claim);
                            }
                        }
                        if(toRemove.size() == 0){
                            gpu.sendMessage(sender, "No claims inside region");
                        }
                        toRemove.forEach(claim -> {
                            gpu.sendMessage(sender, String.format("Deleting claim &a%s", claim.getID()));
                            GriefPrevention.instance.dataStore.deleteClaim(claim);
                        });
                    }
                });
            }
        }
        return true;
    }
}
