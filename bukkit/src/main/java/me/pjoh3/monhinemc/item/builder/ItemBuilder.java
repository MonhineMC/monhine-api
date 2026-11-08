package me.pjoh3.monhinemc.item.builder;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import lombok.NonNull;
import me.pjoh3.monhinemc.item.action.ItemActions;
import me.pjoh3.monhinemc.item.processor.ItemActionProcessor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author pjoh3
 */
@SuppressWarnings("unused")
public class ItemBuilder {
    private final ItemMeta itemMeta;
    private final ItemStack itemStack;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final ItemActions actions = new ItemActions();
    private static final NamespacedKey ACTION_UUID_KEY = NamespacedKey.minecraft("monhine_api_item_action_uuid");

    public ItemBuilder displayName(@NonNull String name) {
        itemMeta.displayName(miniMessage.deserialize(name));
        return this;
    }

    public ItemBuilder lore(@NonNull List<String> list) {
        List<Component> componentList = new ArrayList<>();
        list.forEach(string -> componentList.add(miniMessage.deserialize(string)));
        itemMeta.lore(componentList);
        return this;
    }

    public ItemBuilder unbreakable(boolean value) {
        itemMeta.setUnbreakable(value);
        return this;
    }

    public ItemBuilder headOwner(@NonNull String value, @NonNull String signature) {
        if (!(itemMeta instanceof SkullMeta skullMeta)) {
            throw new IllegalStateException("headOwner() requires a SKULL material");
        }

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), "monhine_custom_head");
        profile.setProperty(new ProfileProperty("textures", value, signature));
        skullMeta.setPlayerProfile(profile);
        return this;
    }

    public ItemBuilder interactAction(@NonNull Consumer<PlayerInteractEvent> action) {
        actions.setInteractAction(action);
        return this;
    }

    public ItemBuilder entityInteractAction(@NonNull Consumer<PlayerInteractEntityEvent> action) {
        actions.setEntityInteractAction(action);
        return this;
    }

    public ItemBuilder pickupAction(@NonNull Consumer<EntityPickupItemEvent> action) {
        actions.setPickupAction(action);
        return this;
    }

    public ItemBuilder dropAction(@NonNull Consumer<PlayerDropItemEvent> action) {
        actions.setDropAction(action);
        return this;
    }

    public ItemBuilder inventoryClickAction(@NonNull Consumer<InventoryClickEvent> action) {
        actions.setInventoryClickAction(action);
        return this;
    }

    public ItemBuilder heldAction(@NonNull Consumer<PlayerItemHeldEvent> action) {
        actions.setHeldAction(action);
        return this;
    }

    protected ItemBuilder(@NonNull Material material) {
        itemStack = new ItemStack(material);
        itemMeta = itemStack.getItemMeta();

        if (this.itemMeta == null) {
            throw new IllegalArgumentException("Material " + material + " has no ItemMeta");
        }
    }

    public static ItemBuilder builder(@NonNull Material material) {
        return new ItemBuilder(material);
    }

    public ItemStack build() {
        String uuid = UUID.randomUUID().toString();
        itemMeta.getPersistentDataContainer().set(ACTION_UUID_KEY, PersistentDataType.STRING, uuid);
        itemStack.setItemMeta(itemMeta);
        ItemActionProcessor.registerAction(uuid, actions);
        return itemStack;
    }

    public static @Nullable String getActionUuid(@Nullable ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir()) {
            return null;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return null;
        }

        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
        if (!persistentDataContainer.has(ACTION_UUID_KEY, PersistentDataType.STRING)) {
            return null;
        }

        return persistentDataContainer.get(ACTION_UUID_KEY, PersistentDataType.STRING);
    }
}
