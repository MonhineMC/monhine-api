package me.vidosya.atwapi.item.processor;

import lombok.experimental.UtilityClass;
import me.vidosya.atwapi.item.action.ItemActions;
import org.bukkit.NamespacedKey;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class ItemActionProcessor {
    private static final Map<String, ItemActions> actionMap = new HashMap<>();

    private String getUUID(@NotNull ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        NamespacedKey key = NamespacedKey.minecraft("atw_api_item_action_uuid");

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if(!container.has(key)) {
            return null;
        }

        return container.get(key, PersistentDataType.STRING);
    }

    private boolean hasAction(@NotNull ItemStack itemStack) {
        return getUUID(itemStack) != null;
    }

    public <T> void acceptAction(T event, ItemStack itemStack) {
        if(itemStack == null || itemStack.isEmpty() || itemStack.getType().isEmpty() || itemStack.getType().isAir()) {
            return;
        }

        if(!hasAction(itemStack)) {
            return;
        }

        ItemActions actions = actionMap.get(getUUID(itemStack));

        if(event instanceof PlayerInteractEvent action) {
            actions.getInteractAction().accept(action);
        } else if(event instanceof PlayerInteractEntityEvent action) {
            actions.getEntityInteractAction().accept(action);
        } else if(event instanceof EntityPickupItemEvent action) {
            actions.getPickupAction().accept(action);
        } else if(event instanceof PlayerDropItemEvent action) {
            actions.getDropAction().accept(action);
        } else if(event instanceof InventoryClickEvent action){
            actions.getInventoryClickAction().accept(action);
        } else if(event instanceof PlayerItemHeldEvent action) {
            actions.getHeldAction().accept(action);
        }
    }

    public static void registerAction(@NotNull String uuid, @NotNull ItemActions actions) {
        actionMap.put(uuid, actions);
    }
}
