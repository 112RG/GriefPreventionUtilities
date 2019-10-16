package _112.griefpreventionutilities.Utils;

import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LocationHelper {

    private UUID worldUniqueId;

    private double maxX;
    private double maxZ;

    private double minX;
    private double minZ;


    public LocationHelper(BlockVector3 firstPoint, BlockVector3 secondPoint, World world) {
        worldUniqueId = world.getUID();

        maxX = Math.max(firstPoint.getBlockX(), secondPoint.getX());
        maxZ = Math.max(firstPoint.getBlockZ(), secondPoint.getBlockZ());

        minX = Math.min(firstPoint.getBlockX(), secondPoint.getBlockX());
        minZ = Math.min(firstPoint.getBlockZ(), secondPoint.getBlockZ());
    }

    public boolean locationIsInRegion(Location loc) {
        return loc.getWorld().getUID().equals(worldUniqueId)
                && loc.getBlockX() > minX && loc.getBlockX() < maxX
                && loc.getBlockZ() > minZ && loc.getBlockZ() < maxZ;
    }

    public static Location locationCenter(World world, Location loc1, Location loc2){
        double x = (loc1.getX() + loc2.getX()) / 2;
        double y = (loc1.getY() + loc2.getY()) / 2;
        double z = (loc1.getZ() + loc2.getZ()) / 2;
        return new Location(world, x,y,z);
    }

    public static void safeTeleport(World world,Location location, Player player){
        double y = world.getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
        player.teleport(new Location(world, location.getX(), y, location.getZ()));
    }

}