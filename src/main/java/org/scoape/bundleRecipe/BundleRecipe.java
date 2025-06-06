package org.scoape.bundleRecipe;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;

public final class BundleRecipe extends JavaPlugin implements Listener {

    private NamespacedKey bk;

    @Override
    public void onEnable() {

        BundleRecipe instance = this;

        Bukkit.getPluginManager().registerEvents(this, this);

        ItemStack bundle = new ItemStack(Material.BUNDLE);
        this.bk = new NamespacedKey(this, "bundle");
        ShapedRecipe bundleRecipe = new ShapedRecipe(bk, bundle);

        bundleRecipe.shape("   ", " S ", " L ");
        bundleRecipe.setIngredient('S', Material.STRING);
        bundleRecipe.setIngredient('L', Material.LEATHER);

        Bukkit.addRecipe(bundleRecipe);

    }

    @Override
    public void onDisable() {

        Bukkit.removeRecipe(new NamespacedKey(this, "bundle"));
        saveConfig();

    }


    @EventHandler
    public void craftItemEvent(PrepareItemCraftEvent event) {

        if(event.getRecipe().getResult().getType()!=Material.BUNDLE) {
            return;
        }

        ItemStack[] craftingGrid = event.getInventory().getMatrix();

        if(matchingRecipe(craftingGrid)) {
            event.getInventory().setResult(new ItemStack(Material.BUNDLE));
        }
    }

    //i used chatgpt for this one xd
    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (event.getRecipe().getResult().getType() == Material.BUNDLE) {
            if (!matchingRecipe(event.getInventory().getMatrix())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void pickUpItem(EntityPickupItemEvent event) {
        if( !(event.getEntity() instanceof Player) ) {
            return;
        }
        else {

            if (!( ((Player) event.getEntity()).hasDiscoveredRecipe(bk))) {
                if(event.getItem().getItemStack().getType()==Material.LEATHER || event.getItem().getItemStack().getType()==Material.STRING) {
                    ((Player) event.getEntity()).discoverRecipe(bk);
                }
            }
        }
    }

    private boolean matchingRecipe(ItemStack[] grid) {

        if(grid[0].getType().equals(Material.STRING) && grid[3].getType().equals(Material.LEATHER) && everythingElseEmpty((ArrayList<Integer>) Arrays.asList(0,3),grid)) {
            return true;
        }

        if(grid[1].getType().equals(Material.STRING) && grid[4].getType().equals(Material.LEATHER) && everythingElseEmpty((ArrayList<Integer>) Arrays.asList(1,4),grid)) {
            return true;
        }

        if(grid[2].getType().equals(Material.STRING) && grid[5].getType().equals(Material.LEATHER) && everythingElseEmpty((ArrayList<Integer>) Arrays.asList(2,5),grid)) {
            return true;
        }

        if(grid[3].getType().equals(Material.STRING) && grid[6].getType().equals(Material.LEATHER) && everythingElseEmpty((ArrayList<Integer>) Arrays.asList(3,6),grid)) {
            return true;
        }

        if(grid[4].getType().equals(Material.STRING) && grid[7].getType().equals(Material.LEATHER) && everythingElseEmpty((ArrayList<Integer>) Arrays.asList(4,7),grid)) {
            return true;
        }

        if(grid[5].getType().equals(Material.STRING) && grid[8].getType().equals(Material.LEATHER) && everythingElseEmpty((ArrayList<Integer>) Arrays.asList(5,8),grid)) {
            return true;
        }

        return false;

    }

    private boolean everythingElseEmpty(ArrayList<Integer> filledSlots, ItemStack[] grid) {
        for(int i=0; i<9; i++) {
            if(filledSlots.contains(i)) {
                continue;
            }
            else {
                if(grid[i].getType().equals(Material.AIR)) {
                    continue;
                }
                else {
                    return false;
                }
            }
        }

        return true;

    }

}
