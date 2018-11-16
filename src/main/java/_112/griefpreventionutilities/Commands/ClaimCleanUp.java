package _112.griefpreventionutilities.Commands;

import com.boydti.fawe.util.TaskManager;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;
import java.util.Date;


public class ClaimCleanUp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if (args[0] != null) {
            Calendar earliestPermissibleLastLogin = Calendar.getInstance();
            earliestPermissibleLastLogin.add(Calendar.DATE, -GriefPrevention.instance.config_claims_expirationDays);
            TaskManager.IMP.async(new BukkitRunnable() {
                @Override
                public void run() {
                    int count = 0;
                    for (Claim claim : GriefPrevention.instance.dataStore.getClaims()) {
                        if (earliestPermissibleLastLogin.getTime().after(new Date(Bukkit.getPlayer(claim.ownerID).getLastPlayed()))) {
                            count = count + 1;
                            continue;
                        }
                    }
                    player.sendRawMessage("There are " + count + " claims where the owner hasn't logged in " + args[0] + " days");
                }
            });
        } else {
                player.sendRawMessage("Please specify a number of days /claimcleanup <number of days>");
            }
        }
        return true;

    }

}
