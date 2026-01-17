package me.lightgr3y.di;

/**
 * @author Light_Gr3y
 */
@SuppressWarnings("unused")
public interface LifecycleService {
    default void enable() {}
    default void disable() {}
}
