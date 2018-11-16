package _112.griefpreventionutilities.Commands;

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

import java.text.MessageFormat;

public class CountClaims implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(sender instanceof Player) {
            FawePlayer fawePlayer = FawePlayer.wrap(sender);
            World world = Bukkit.getWorld(fawePlayer.getWorld().getName());
            if (fawePlayer.getSelection() != null) {
            Region region = new Region(fawePlayer.getSelection().getMinimumPoint(), fawePlayer.getSelection().getMaximumPoint(), world);
            /* TODO fix async WE warning? */
            TaskManager.IMP.async(new BukkitRunnable() {
                @Override
                public void run() {
                    int count = 0;
                    for (Claim claim : GriefPrevention.instance.dataStore.getClaims()) {
                        if (region.locationIsInRegion(claim.getLesserBoundaryCorner()) && region.locationIsInRegion(claim.getGreaterBoundaryCorner())) {
                            count++;
                        }
                    }
                    fawePlayer.sendMessage("There are " + count + " in your current selection");
                }
            });

        }
        }
        return true;
    }
}