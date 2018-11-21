package _112.griefpreventionutilities.Commands;

import _112.griefpreventionutilities.Utils.PrivateInventory;
import _112.griefpreventionutilities.Utils.LocationHelper;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class EClaims implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (Bukkit.getPlayer(args[0]) != null) {
                Vector<Claim> claims = GriefPrevention.instance.dataStore.getPlayerData(Bukkit.getPlayer(args[0]).getUniqueId()).getClaims();
                claimGui(((Player) sender), claims);
            }
        }
        return true;
    }


    public void claimGui(Player player, Vector<Claim> claims){
        PrivateInventory inventory = new PrivateInventory(String.format("Viewing %s claims", player.getName()), 54, player.getUniqueId(), PrivateInventory.placeholder(DyeColor.BLACK, ChatColor.translateAlternateColorCodes('&', "&cPLACEHOLDER")));
        int slot = 0;
        for (Claim claim : claims) {
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.translateAlternateColorCodes('&', String.format("&cLocation:&a %S %S %S", claim.getLesserBoundaryCorner().getBlockX(), claim.getLesserBoundaryCorner().getBlockY(), claim.getLesserBoundaryCorner().getBlockZ())));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&cArea:&a " + claim.getArea()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&cSubdivisions:&a " + claim.children.size()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&cManagers:&a " + claim.managers.size()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&cExplosions:&a " + claim.areExplosivesAllowed));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&cDoors Open:&a " + claim.doorsOpen));

            inventory.setItem(PrivateInventory.claimItem(Material.GRASS_BLOCK, claim.getID().toString(), lore), claim.getID().toString(), slot, new PrivateInventory.ClickRunnable() {
                @Override
                public void run(InventoryClickEvent e) {
                    utilGui(player, claim);
                }
            }, null);
            slot++;
        }
        inventory.openInventory(player);
    }

    public void utilGui(Player player, Claim claim){
        PrivateInventory inventory = new PrivateInventory(String.format("Viewing claim %s owner %s", claim.getID(), player.getName()), 54, player.getUniqueId(), PrivateInventory.placeholder(DyeColor.BLACK, "No claims no games"));
        inventory.setItem(PrivateInventory.utilItem(ChatColor.GREEN + "Teleport", Material.ENDER_PEARL), 11, new PrivateInventory.ClickRunnable() {
            @Override
            public void run(InventoryClickEvent event) {
                Location tp = LocationHelper.locationCenter(claim.getGreaterBoundaryCorner().getWorld(), claim.getLesserBoundaryCorner(), claim.getGreaterBoundaryCorner());
                LocationHelper.safeTeleport(claim.getGreaterBoundaryCorner().getWorld(), tp, player);
            }
        });
        inventory.setItem(PrivateInventory.utilItem(ChatColor.GREEN + "Delete claim", Material.BARRIER), 13, new PrivateInventory.ClickRunnable() {
            @Override
            public void run(InventoryClickEvent event) {
                GriefPrevention.instance.dataStore.deleteClaim(claim);
            }
        });
        inventory.setItem(PrivateInventory.utilItem(ChatColor.RED + "Back", Material.RED_STAINED_GLASS_PANE), 45, new PrivateInventory.ClickRunnable() {
            @Override
            public void run(InventoryClickEvent event) {
                claimGui(player, GriefPrevention.instance.dataStore.getPlayerData(claim.ownerID).getClaims());
            }
        });

        inventory.openInventory(player);
    }
}
