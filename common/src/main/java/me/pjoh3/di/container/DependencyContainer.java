package me.pjoh3.di.container;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.pjoh3.di.LifecycleService;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pjoh3
 */
@UtilityClass
public class DependencyContainer {
    @Getter
    private final Map<Class<?>, Object> dependencyMap = new ConcurrentHashMap<>();

    public void registerDependency(@NotNull Class<?> clazz, @NotNull Object object) {
        dependencyMap.put(clazz, object);

        if (object instanceof LifecycleService) {
            ((LifecycleService) object).enable();
        }
    }

    public boolean isDependencyRegistered(@NotNull Class<?> clazz) {
        return dependencyMap.containsKey(clazz);
    }

    public Object getDependency(@NotNull Class<?> clazz) {
        return dependencyMap.get(clazz);
    }
}
