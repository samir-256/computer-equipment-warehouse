package com.warehouse.startup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

final class Instances {

    private Instances() {
    }

    static <T> T create(String className, Class<T> type, Object... arguments) {
        Class<?> loaded = load(className);
        if (!type.isAssignableFrom(loaded)) {
            throw new IllegalStateException(
                    "Class " + className + " is not a " + type.getSimpleName());
        }
        try {
            Constructor<?> constructor = loaded.getConstructor(parameterTypes(arguments));
            return type.cast(constructor.newInstance(arguments));
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Class " + className + " has no public constructor for "
                    + Arrays.toString(parameterTypes(arguments)), e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("Cannot create " + className + ": " + e.getCause(), e.getCause());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Cannot create " + className, e);
        }
    }

    private static Class<?> load(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Class not found: " + className, e);
        }
    }

    private static Class<?>[] parameterTypes(Object[] arguments) {
        return Arrays.stream(arguments).map(Object::getClass).toArray(Class<?>[]::new);
    }
}
