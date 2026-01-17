package me.lightgr3y.di.injector;

import me.lightgr3y.di.provider.DependencyProvider;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface DependencyInjector {
    void setProvider(@NotNull DependencyProvider provider);

    void init(@NotNull String basePackage);
}
