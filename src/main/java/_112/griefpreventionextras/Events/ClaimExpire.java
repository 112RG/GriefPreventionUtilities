package _112.griefpreventionextras.Events;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.EditSessionBuilder;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import me.ryanhamshire.GriefPrevention.events.ClaimDeletedEvent;
import me.ryanhamshire.GriefPrevention.events.ClaimExpirationEvent;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ClaimExpire implements Listener {

    @EventHandler
    public void onClaimExpire(ClaimExpirationEvent event){
        World world = FaweAPI.getWorld(event.getClaim().getGreaterBoundaryCorner().getWorld().getName());
        EditSession editSession = new EditSessionBuilder(world).fastmode(true).build();

        Location corner1 = event.getClaim().getLesserBoundaryCorner();
        Location corner2 = event.getClaim().getGreaterBoundaryCorner();

        Vector pos1 = new Vector(corner1.getBlockX(), world.getMaxY(), corner1.getBlockZ());
        Vector pos2 = new Vector(corner2.getBlockX(), 0, corner2.getBlockZ());
        Region region = new CuboidRegion(pos1, pos2);

        editSession.regenerate(region, editSession);
        editSession.flushQueue();
    }

    @EventHandler
    public void onClaimDelete(ClaimDeletedEvent event){

    }
}
