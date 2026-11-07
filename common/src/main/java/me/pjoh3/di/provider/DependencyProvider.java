package me.pjoh3.di.provider;

import me.pjoh3.di.container.DependencyContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pjoh3
 */
@SuppressWarnings("unused")
public abstract class DependencyProvider {
    private final Map<Class<?>, Object> dependencyMap = new ConcurrentHashMap<>();

    public void enable() {
        dependencyMap.forEach(DependencyContainer::registerDependency);
    }

    public void registerDependency(@NotNull Class<?> clazz, @NotNull Object instance) {
        dependencyMap.put(clazz, instance);
    }
}
