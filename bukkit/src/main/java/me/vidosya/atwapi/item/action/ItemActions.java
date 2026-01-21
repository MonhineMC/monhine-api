package me.vidosya.atwapi.item.action;

import lombok.Data;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

import java.util.function.Consumer;

@Data
public class ItemActions {
    // Данный класс был взят у моего хорошего друга https://github.com/Dwyur
    // Спасибо ему за помощь и обучение в мире кода!
    private Consumer<PlayerInteractEvent> interactAction = playerInteractEvent -> {};
    private Consumer<PlayerInteractEntityEvent> entityInteractAction = playerInteractEntityEvent -> {};

    private Consumer<EntityPickupItemEvent> pickupAction = entityPickupItemEvent -> {};
    private Consumer<PlayerDropItemEvent> dropAction = playerDropItemEvent -> {};

    private Consumer<InventoryClickEvent> inventoryClickAction = inventoryClickEvent -> {};

    private Consumer<PlayerItemHeldEvent> heldAction = playerItemHeldEvent -> {};
}