package me.lightgr3y.di.injector;

import lombok.Getter;
import lombok.SneakyThrows;
import me.lightgr3y.di.annotation.Inject;
import me.lightgr3y.di.constructor.ConstructorSorter;
import me.lightgr3y.di.container.DependencyContainer;
import me.lightgr3y.di.provider.DependencyProvider;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class AbstractDependencyInjector implements DependencyInjector {
    @Getter
    private Reflections reflections;
    private DependencyProvider provider;

    @Override
    public void setProvider(@NotNull DependencyProvider provider) {
        this.provider = provider;
    }

    @SneakyThrows
    public void init(@NotNull String basePackage) {
        provider.enable();

        reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(basePackage))
                .setScanners(Scanners.ConstructorsAnnotated)
        );

        Set<Constructor> constructorSet = reflections.getConstructorsAnnotatedWith(Inject.class);
        List<Constructor<?>> sortedConstructors = ConstructorSorter.createOrder(constructorSet);
        sortedConstructors.forEach(this::processConstructor);
    }

    @SneakyThrows
    private void processConstructor(Constructor<?> constructor) {
        Parameter[] parameters = constructor.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> paramType = parameter.getType();

            Object dependency = DependencyContainer.getDependency(paramType);
            if (dependency == null) {
                throw new IllegalStateException("Зависимость не найдена для типа: " + paramType);
            }

            args[i] = dependency;
        }

        Object object = constructor.newInstance(args);
        DependencyContainer.registerDependency(object.getClass(), object);
    }
}
