package _112.griefpreventionextras.Commands;

import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class ClaimCleanUp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), true, null);

        }
        return true;

    }

}
