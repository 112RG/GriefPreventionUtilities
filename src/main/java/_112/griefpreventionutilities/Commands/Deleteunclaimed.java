package _112.griefpreventionutilities.Commands;

import com.boydti.fawe.jnbt.anvil.MCAFile;
import com.boydti.fawe.jnbt.anvil.MCAFilter;
import com.boydti.fawe.jnbt.anvil.MCAQueue;
import com.boydti.fawe.jnbt.anvil.filters.DeleteUnclaimedFilter;
import com.boydti.fawe.object.FawePlayer;
import com.boydti.fawe.object.FaweQueue;
import com.boydti.fawe.util.SetQueue;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class Deleteunclaimed implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(Bukkit.getServer().getWorld(args[0]) != null){
            FaweQueue faweQueue = SetQueue.IMP.getNewQueue(args[0], true, false);
            MCAQueue mcaQueue = new MCAQueue(faweQueue);
            DeleteUnclaimedFilter  deleteUnclaimedFilter = new DeleteUnclaimedFilter(FawePlayer.wrap(sender).getWorld(), 60000, 60000, 60000);
            MCAFilter mcaFilter = mcaQueue.filterWorld(deleteUnclaimedFilter);
            Bukkit.getLogger().info(String.valueOf(((DeleteUnclaimedFilter) mcaFilter).getTotal()));
            mcaQueue.flush();
        }
        return true;
    }
}
