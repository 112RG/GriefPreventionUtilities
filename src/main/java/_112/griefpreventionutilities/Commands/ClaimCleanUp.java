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
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class ClaimCleanUp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            GriefPreventionUtilities gpu = GriefPreventionUtilities.getPlugin();
            if (args.length == 1 && args[0] != null) {
                try{
                    Integer.parseInt(args[0]);
                } catch (NumberFormatException e){
                    gpu.sendMessage(sender, String.format("&c%s&r is not a integer", args[0]));
                    return true;
                }
            Calendar earliestPermissibleLastLogin = Calendar.getInstance();
            earliestPermissibleLastLogin.add(Calendar.DATE, -GriefPrevention.instance.config_claims_expirationDays);
            TaskManager.IMP.async(new BukkitRunnable() {
                @Override
                public void run() {
                    int count = 0;
                    ArrayList<UUID> uuid = new ArrayList<>();
                    for (Claim claim : GriefPrevention.instance.dataStore.getClaims()) {
                        if (earliestPermissibleLastLogin.getTime().after(new Date(Bukkit.getPlayer(claim.ownerID).getLastPlayed()))) {
                            count = count + 1;
                            if(!uuid.contains(claim.ownerID)){
                                uuid.add(claim.ownerID);
                                Bukkit.getLogger().info(String.format("Deleting claims for %s", claim.ownerID));
                            }
                            continue;
                        }
                    }

                    gpu.sendMessage(sender, String.format("&a%s&r claims where the owner hasn't logged in. In &a%s&r days", count, args[0]));

                }
            });
        } else {
                gpu.sendMessage(sender,"Please specify numfer of days /claimcleanup <number of days>");
            }
        }
        return true;

    }

}
