package _112.griefpreventionutilities.Util;

import com.sk89q.worldedit.Vector;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

public class Region {

    private UUID worldUniqueId;

    private double maxX;
    private double maxZ;

    private double minX;
    private double minZ;


    public Region(Vector firstPoint, Vector secondPoint, World world) {
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

}