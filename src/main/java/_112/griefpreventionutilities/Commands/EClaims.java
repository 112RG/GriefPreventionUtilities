package _112.griefpreventionutilities.Commands;

import _112.griefpreventionutilities.Utils.PrivateInventory;
import _112.griefpreventionutilities.Utils.LocationHelper;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class EClaims implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (Bukkit.getPlayer(args[0]) != null) {
                Vector<Claim> claims = GriefPrevention.instance.dataStore.getPlayerData(Bukkit.getPlayer(args[0]).getUniqueId()).getClaims();

                Player player = ((Player) sender);
                PrivateInventory inventory = new PrivateInventory(String.format("Viewing %s claims", args[0]), 54, ((Player) sender).getUniqueId(), PrivateInventory.placeholder(DyeColor.BLACK, "No claims no games"));
                int slot = 0;
                for (Claim claim : claims) {
                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.translateAlternateColorCodes('&', String.format("&cLocation:&a %S %S %S", claim.getLesserBoundaryCorner().getBlockX(), claim.getLesserBoundaryCorner().getBlockY(), claim.getLesserBoundaryCorner().getBlockZ())));
                    lore.add(ChatColor.translateAlternateColorCodes('&', "&cArea:&a " + claim.getArea()));
                    lore.add(ChatColor.translateAlternateColorCodes('&', "&cSubdivisions:&a " + claim.children.size()));
                    lore.add(ChatColor.translateAlternateColorCodes('&', "&cManagers:&a " + claim.managers.size()));
                    lore.add(ChatColor.translateAlternateColorCodes('&', "&cExplosions:&a " + claim.areExplosivesAllowed));
                    lore.add(ChatColor.translateAlternateColorCodes('&', "&cDoors Open:&a " + claim.doorsOpen));

                    inventory.setItem(PrivateInventory.claimItem(claim.getID().toString(), lore), claim.getID().toString(), slot, new PrivateInventory.ClickRunnable() {
                        @Override
                        public void run(InventoryClickEvent e) {
                            Location tp = LocationHelper.locationCenter(claim.getGreaterBoundaryCorner().getWorld(), claim.getLesserBoundaryCorner(), claim.getGreaterBoundaryCorner());
                            LocationHelper.safeTeleport(claim.getGreaterBoundaryCorner().getWorld(), tp, player);
                        }
                    }, null);
                    slot++;
                }
                inventory.openInventory(player);
            }

        }

        return true;
    }
}
