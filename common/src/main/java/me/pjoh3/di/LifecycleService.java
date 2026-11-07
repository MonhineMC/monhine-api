package me.pjoh3.di;

/**
 * @author pjoh3
 */
@SuppressWarnings("unused")
public interface LifecycleService {
    default void enable() {}
    default void disable() {}
}
