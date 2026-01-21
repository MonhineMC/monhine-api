package me.vidosya.atwapi.item.listener;

import me.vidosya.atwapi.item.processor.ItemActionProcessor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

/**
 * @author Light_Gr3y
 */
public class ItemListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ItemActionProcessor.acceptAction(event, event.getItem());
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        ItemActionProcessor.acceptAction(event, event.getPlayer().getActiveItem());
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        ItemActionProcessor.acceptAction(event, event.getItem().getItemStack());
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        ItemActionProcessor.acceptAction(event, event.getItemDrop().getItemStack());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemActionProcessor.acceptAction(event, event.getCurrentItem());
    }

    @EventHandler
    public void onHeld(PlayerItemHeldEvent event) {
        ItemActionProcessor.acceptAction(event, event.getPlayer().getActiveItem());
    }
}
