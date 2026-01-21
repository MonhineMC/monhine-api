package me.lightgr3y.di.constructor;

import lombok.experimental.UtilityClass;
import me.lightgr3y.di.annotation.Inject;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @author Deepseek
 * На вайб кодил, двур убери за мной
 */
@UtilityClass
public class ConstructorSorter {
    public List<Constructor<?>> createOrder(@NotNull Set<Constructor> constructors) {
        Set<Class<?>> classes = new HashSet<>();

        for (Constructor constructor : constructors) {
            classes.add(constructor.getDeclaringClass());
        }

        Map<Class<?>, List<Class<?>>> dependenciesMap = buildDependenciesMap(classes);
        List<Class<?>> sortedClasses = topologicalSort(dependenciesMap);

        return getConstructorsFromClasses(sortedClasses, constructors);
    }

    private Map<Class<?>, List<Class<?>>> buildDependenciesMap(Set<Class<?>> classes) {
        Map<Class<?>, List<Class<?>>> dependenciesMap = new HashMap<>();

        for (Class<?> clazz : classes) {
            Constructor<?> injectConstructor = findInjectConstructor(clazz);
            if (injectConstructor == null) {
                continue;
            }

            List<Class<?>> dependencies = getDependencies(injectConstructor, classes);
            dependenciesMap.put(clazz, dependencies);
        }

        return dependenciesMap;
    }

    private Constructor<?> findInjectConstructor(Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                return constructor;
            }
        }
        return null;
    }

    private List<Class<?>> getDependencies(Constructor<?> constructor, Set<Class<?>> allClasses) {
        List<Class<?>> dependencies = new ArrayList<>();

        for (Parameter parameter : constructor.getParameters()) {
            Class<?> paramType = parameter.getType();

            if (allClasses.contains(paramType)) {
                dependencies.add(paramType);
            }
        }

        return dependencies;
    }

    private List<Class<?>> topologicalSort(Map<Class<?>, List<Class<?>>> dependenciesMap) {
        List<Class<?>> result = new ArrayList<>();
        Set<Class<?>> visited = new HashSet<>();
        Set<Class<?>> visiting = new HashSet<>();

        for (Class<?> node : dependenciesMap.keySet()) {
            if (!visited.contains(node)) {
                if (visit(node, dependenciesMap, visited, visiting, result)) {
                    throw new IllegalStateException("Обнаружена циклическая зависимость для класса: " + node.getName());
                }
            }
        }

        return result;
    }

    private boolean visit(Class<?> node,
                          Map<Class<?>, List<Class<?>>> dependenciesMap,
                          Set<Class<?>> visited,
                          Set<Class<?>> visiting,
                          List<Class<?>> result) {

        if (visiting.contains(node)) {
            return true;
        }

        if (visited.contains(node)) {
            return false;
        }

        visiting.add(node);

        List<Class<?>> dependencies = dependenciesMap.getOrDefault(node, Collections.emptyList());
        for (Class<?> dependency : dependencies) {
            if (visit(dependency, dependenciesMap, visited, visiting, result)) {
                return true;
            }
        }

        visiting.remove(node);
        visited.add(node);
        result.add(node);

        return false;
    }

    private List<Constructor<?>> getConstructorsFromClasses(List<Class<?>> sortedClasses, Set<Constructor> originalConstructors) {
        List<Constructor<?>> sortedConstructors = new ArrayList<>();

        Map<Class<?>, Constructor<?>> constructorMap = new HashMap<>();
        for (Constructor constructor : originalConstructors) {
            constructorMap.put(constructor.getDeclaringClass(), constructor);
        }

        for (Class<?> clazz : sortedClasses) {
            Constructor<?> constructor = constructorMap.get(clazz);
            if (constructor != null) {
                sortedConstructors.add(constructor);
            }
        }

        return sortedConstructors;
    }
}
