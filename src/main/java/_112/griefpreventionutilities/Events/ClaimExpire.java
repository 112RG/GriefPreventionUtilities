package _112.griefpreventionutilities.Events;

import _112.griefpreventionutilities.GriefPreventionUtilities;
import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.events.ClaimDeletedEvent;
import me.ryanhamshire.GriefPrevention.events.ClaimExpirationEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ClaimExpire implements Listener {

    @EventHandler
    public void onClaimExpire(ClaimExpirationEvent event) {
        if(GriefPreventionUtilities.getPlugin().getConfig().get("claimexpire.action").equals("regen")){
            Claim claim = event.getClaim();
            Bukkit.getLogger().info(String.format("Claim %s:%s expired running regen at %s", claim.getOwnerName(), claim.getID().toString(), claim.getLesserBoundaryCorner().toString()));
            World world = FaweAPI.getWorld(event.getClaim().getGreaterBoundaryCorner().getWorld().getName());
            EditSession editSession = new EditSessionBuilder(world).fastmode(true).build();

            Location corner1 = claim.getLesserBoundaryCorner();
            Location corner2 = claim.getGreaterBoundaryCorner();

            Vector pos1 = new Vector(corner1.getBlockX(), world.getMaxY(), corner1.getBlockZ());
            Vector pos2 = new Vector(corner2.getBlockX(), 0, corner2.getBlockZ());
            Region region = new CuboidRegion(pos1, pos2);

            editSession.regenerate(region, editSession);
            editSession.flushQueue();
        }
        else if(GriefPreventionUtilities.getPlugin().getConfig().get("claimexpire.action").equals("restore")){

        }

    }

    @EventHandler
    public void onClaimDelete(ClaimDeletedEvent event) {
        String action;
        if(GriefPreventionUtilities.getPlugin().getConfig().get("claimexpire.action") != null){
            action = GriefPreventionUtilities.getPlugin().getConfig().get("claimexpire.action").toString();
        } else {
            action = "nothing";
        }

        if(action.equals("regen")){
            Claim claim = event.getClaim();
            GriefPreventionUtilities.getPlugin().logMessage(String.format("Claim %s:%s expired running regen at %s", claim.getOwnerName(), claim.getID().toString(), claim.getLesserBoundaryCorner().toString()));
            World world = FaweAPI.getWorld(event.getClaim().getGreaterBoundaryCorner().getWorld().getName());
            EditSession editSession = new EditSessionBuilder(world).fastmode(true).build();

            Location corner1 = claim.getLesserBoundaryCorner();
            Location corner2 = claim.getGreaterBoundaryCorner();

            Vector pos1 = new Vector(corner1.getBlockX(), world.getMaxY(), corner1.getBlockZ());
            Vector pos2 = new Vector(corner2.getBlockX(), 0, corner2.getBlockZ());
            Region region = new CuboidRegion(pos1, pos2);

            editSession.regenerate(region, editSession);
            editSession.flushQueue();
        }
        else if(action.equals("restore")){

        }
    }
}
