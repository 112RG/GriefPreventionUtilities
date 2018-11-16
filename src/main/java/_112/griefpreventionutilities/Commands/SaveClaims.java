package _112.griefpreventionutilities.Commands;

import com.boydti.fawe.object.schematic.Schematic;
import com.boydti.fawe.util.EditSessionBuilder;
import com.boydti.fawe.util.TaskManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.regions.CuboidRegion;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

public class SaveClaims implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args[0] != null && args[1] != null){
            if(player.getServer().getWorld(args[0]) != null && player.getServer().getWorld(args[1]) != null){
                        TaskManager.IMP.async(new Runnable() {
                    @Override
                    public void run() {
                        Location corner1;
                        Location corner2;
                        World world1;
                        World world2;
                        int claims = GriefPrevention.instance.dataStore.getClaims().size();
                        int done = 0;
                        for(Claim claim : GriefPrevention.instance.dataStore.getClaims()){
                            corner1 = claim.getLesserBoundaryCorner();
                            corner2 = claim.getGreaterBoundaryCorner();
                            world1 = Bukkit.getWorld(args[0]);
                            world2 = Bukkit.getWorld(args[1]);
                            if(!corner1.getWorld().getName().equals(args[0])){
                                Bukkit.getLogger().info(MessageFormat.format("Skipping claim in {0} doesn't match specified world {1}", corner1.getWorld().getName(), world1.getName()));
                                continue;

                            }
               /*             Calendar earliestPermissibleLastLogin = Calendar.getInstance();
                            earliestPermissibleLastLogin.add(Calendar.DATE, -GriefPrevention.instance.config_claims_expirationDays);
                            if(earliestPermissibleLastLogin.getTime().after(new Date(Bukkit.getPlayer(claim.ownerID).getLastPlayed())))
                            {
                                player.getServer().getConsoleSender().sendMessage("Skipping claim as player" + claim.getOwnerName() + " hasn't logged in for " + GriefPrevention.instance.config_claims_expirationDays);
                                continue;
                            }
*/

                                Bukkit.getLogger().info(MessageFormat.format("Copying claim {0}:{1} from {2} {3} {4}", claim.getOwnerName(), claim.getID(), corner1.getBlockX(), corner1.getBlockY(), corner1.getBlockZ()));

                                EditSession copyWorld = new EditSessionBuilder(world1.getName()).autoQueue(false).build();
                                EditSession pasteWorld = new EditSessionBuilder(world2.getName()).build();
                                Vector pos1 = new Vector(corner1.getBlockX(), 0, corner1.getBlockZ());
                                Vector pos2 = new Vector(corner2.getBlockX(), copyWorld.getMaxY(), corner2.getBlockZ());
                                CuboidRegion copyRegion = new CuboidRegion(pos1, pos2);

                                BlockArrayClipboard lazyCopy = copyWorld.lazyCopy(copyRegion);

                                Schematic schem = new Schematic(lazyCopy);
                                boolean pasteAir = true;
                                Vector to = new Vector(corner1.getBlockX(), 0, corner1.getBlockZ());
                                schem.paste(pasteWorld, to, pasteAir);
                                pasteWorld.flushQueue();

                                Bukkit.getLogger().info(MessageFormat.format("Copied claim {0}", claim.getID()));
                                claims = claims - 1;
                                done = done + 1;
                                if(done == 10){
                                    done = 0;
                                    Bukkit.getLogger().info(MessageFormat.format("{0} claims left to copy", claims));

                                }

                        }
                        }

                });

            } else {
                player.sendRawMessage("A world you specified doesn't exist");
                return true;
            }
        }
            else {
                player.sendRawMessage("Please specify two worlds /saveclaims <world1> <world2>");
                return true;
            }

    }
        return true;
    }
}
