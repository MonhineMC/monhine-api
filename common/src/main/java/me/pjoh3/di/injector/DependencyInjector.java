package me.pjoh3.di.injector;

import me.pjoh3.di.provider.DependencyProvider;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface DependencyInjector {
    void setProvider(@NotNull DependencyProvider provider);

    void init(@NotNull String basePackage);
}
