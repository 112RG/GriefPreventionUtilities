package _112.griefpreventionutilities.Commands;

import _112.griefpreventionutilities.GriefPreventionUtilities;
import _112.griefpreventionutilities.Utils.PrivateInventory;
import _112.griefpreventionutilities.Utils.LocationHelper;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

public class EClaims implements CommandExecutor {
    private GriefPreventionUtilities gpu = GriefPreventionUtilities.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length >= 1 && Bukkit.getOfflinePlayer(args[0]) != null) {
                if (!sender.hasPermission("griefpreventionutils.eclaims.admin")) return true;
                Vector<Claim> claims = GriefPrevention.instance.dataStore.getPlayerData(Bukkit.getOfflinePlayer(args[0]).getUniqueId()).getClaims();
                claimGui(((Player) sender), claims);
            } else {
                Vector<Claim> claims = GriefPrevention.instance.dataStore.getPlayerData(Bukkit.getOfflinePlayer(((Player) sender).getUniqueId()).getUniqueId()).getClaims();
                claimGui(((Player) sender), claims);
            }
        }
        return true;
    }


    public void claimGui(Player player, Vector<Claim> claims) {
        PlayerData playerData = GriefPrevention.instance.dataStore.getPlayerData(player.getUniqueId());
        PrivateInventory inventory = new PrivateInventory(String.format("%s: %s/%s + %s", player.getName(), playerData.getAccruedClaimBlocks(), playerData.getAccruedClaimBlocksLimit(), playerData.getBonusClaimBlocks()), 54, player.getUniqueId(), PrivateInventory.placeholder(DyeColor.BLACK, ChatColor.translateAlternateColorCodes('&', "&cPLACEHOLDER")));
        int slot = 0;
        for (Claim claim : claims) {
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.translateAlternateColorCodes('&', String.format("&cLocation:&a %S %S %S", claim.getLesserBoundaryCorner().getBlockX(), claim.getLesserBoundaryCorner().getBlockY(), claim.getLesserBoundaryCorner().getBlockZ())));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&cSize:&a " + claim.getArea()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&cSubdivisions:&a " + claim.children.size()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&cManagers:&a " + claim.managers.size()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&cExplosions:&a " + claim.areExplosivesAllowed));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&cDoors Open:&a " + claim.doorsOpen));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eClick me for more"));


            inventory.setItem(PrivateInventory.claimItem(Material.GRASS_BLOCK, claim.getID().toString(), lore), claim.getID().toString(), slot, new PrivateInventory.ClickRunnable() {
                @Override
                public void run(InventoryClickEvent e) {


                    if (player.hasPermission("griefpreventionutils.eclaims.admin")) {
                        utilGui(player, claim);
                    } else {
                        player.closeInventory();
                        trustMessage(player, claim);
                    }
                }
            }, null);
            slot++;
        }
        inventory.openInventory(player);
    }

    public void utilGui(Player player, Claim claim) {
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
                gpu.logMessage(String.format("%s deleted a claim. Owner: %s ClaimID: %s", player.getName(), claim.getOwnerName(), claim.getID()));
                player.closeInventory();

            }
        });
        inventory.setItem(PrivateInventory.utilItem(ChatColor.GREEN + "Trust list", Material.BOOK), 15, new PrivateInventory.ClickRunnable() {
            @Override
            public void run(InventoryClickEvent event) {
                player.closeInventory();
                trustMessage(player, claim);
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

    public void trustMessage(Player player, Claim claim) {
        ArrayList<String> builders = new ArrayList<String>();
        ArrayList<String> containers = new ArrayList<String>();
        ArrayList<String> accessors = new ArrayList<String>();
        ArrayList<String> managers = new ArrayList<String>();
        claim.getPermissions(builders, containers, accessors, managers);

        StringBuilder permissions = new StringBuilder();

        permissions.append(ChatColor.GOLD + "Managers: ");

        if (managers.size() > 0) {
            for (int i = 0; i < managers.size(); i++)
                permissions.append(gpu.getServer().getOfflinePlayer(UUID.fromString(managers.get(i))).getName() + " ");
        }

        gpu.sendMessage(player, permissions.toString());
        permissions = new StringBuilder();
        permissions.append(ChatColor.YELLOW + "Builders: ");

        if (builders.size() > 0) {
            for (int i = 0; i < builders.size(); i++)
                permissions.append(gpu.getServer().getOfflinePlayer(UUID.fromString(builders.get(i))).getName() + " ");
        }

        gpu.sendMessage(player, permissions.toString());
        permissions = new StringBuilder();
        permissions.append(ChatColor.GREEN + "Containers: ");

        if (containers.size() > 0) {
            for (int i = 0; i < containers.size(); i++)
                permissions.append(gpu.getServer().getOfflinePlayer(UUID.fromString(containers.get(i))).getName() + " ");
        }

        gpu.sendMessage(player, permissions.toString());
        permissions = new StringBuilder();
        permissions.append(ChatColor.BLUE + "Accessors: ");

        if (accessors.size() > 0) {
            for (int i = 0; i < accessors.size(); i++)
                permissions.append(gpu.getServer().getOfflinePlayer(accessors.get(i)).getName() + " ");
        }

        gpu.sendMessage(player, permissions.toString());
    }
}
