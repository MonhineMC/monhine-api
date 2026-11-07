package me.pjoh3.monhinemc.di.injector;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolverBase;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.litecommands.bukkit.LiteBukkitSettings;
import lombok.Setter;
import me.pjoh3.di.annotation.command.NArgument;
import me.pjoh3.di.annotation.command.NCommand;
import me.pjoh3.di.container.DependencyContainer;
import me.pjoh3.di.injector.AbstractDependencyInjector;
import me.pjoh3.monhinemc.command.litecommands.locale.LiteCommandsLocale;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author pjoh3
 */
public class BukkitDependencyInjector extends AbstractDependencyInjector {
    private final Plugin plugin;

    @Setter
    private LiteCommandsLocale liteCommandsLocale;

    public BukkitDependencyInjector(Plugin plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("all")
    public void enable(@NotNull String basePackage) {
        init(basePackage);

        List<Object> liteCommandsArray = new ArrayList<>();
        DependencyContainer.getDependencyMap().forEach((aClass, object) -> {
            if (object instanceof Listener) {
                plugin.getServer().getPluginManager().registerEvents((Listener) object, plugin);
            }

            if (!aClass.isAnnotationPresent(NCommand.class)) {
                return;
            }

            liteCommandsArray.add(object);
        });

        if (liteCommandsArray.isEmpty()) {
            return;
        }

        LiteCommandsBuilder<CommandSender, LiteBukkitSettings, ?> builder = LiteBukkitFactory
                .builder(plugin.getName(), plugin).commands(liteCommandsArray.toArray());

        if (liteCommandsLocale != null) {
            builder.message(LiteBukkitMessages.INVALID_USAGE, liteCommandsLocale.getInvalidUsage());
            builder.message(LiteBukkitMessages.PLAYER_ONLY, liteCommandsLocale.getPlayerOnly());
            builder.message(LiteBukkitMessages.PLAYER_NOT_FOUND, liteCommandsLocale.getPlayerNotFound());
            builder.message(LiteBukkitMessages.MISSING_PERMISSIONS, liteCommandsLocale.getMissingPermissions());
            builder.message(LiteBukkitMessages.WORLD_NOT_EXIST, liteCommandsLocale.getWorldNotExist());
            builder.message(LiteBukkitMessages.WORLD_PLAYER_ONLY, liteCommandsLocale.getWorldPlayerOnly());
            builder.message(LiteBukkitMessages.UUID_INVALID_FORMAT, liteCommandsLocale.getUuidInvalidFormat());
            builder.message(LiteBukkitMessages.LOCATION_INVALID_FORMAT, liteCommandsLocale.getLocationInvalidFormat());
            builder.message(LiteBukkitMessages.INVALID_NUMBER, liteCommandsLocale.getInvalidNumber());
            builder.message(LiteBukkitMessages.INSTANT_INVALID_FORMAT, liteCommandsLocale.getInstantInvalidFormat());
        }

        Reflections reflections = getReflections();
        Set<Class<?>> liteCommandsArgumentClassesSet = reflections.getTypesAnnotatedWith(NArgument.class);

        liteCommandsArgumentClassesSet.forEach(clazz -> {
            if (!DependencyContainer.isDependencyRegistered(clazz)) {
                return;
            }

            NArgument argumentAnnotation = clazz.getAnnotation(NArgument.class);
            assert argumentAnnotation != null;

            builder.argument(argumentAnnotation.argument(), (ArgumentResolverBase) DependencyContainer.getDependency(clazz));
        });

        builder.build();
    }
}
