package com.maingirder.oauth.core.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author maxy
 * @since 1.0.0
 */
public class ClassLoaderUtils {

    private ClassLoaderUtils() {
    }

    /**
     * 获取接口的实现类集合
     *
     * @param cls
     * @return
     * @throws Exception
     */
    public static List<Class<?>> getListByClass(Class<?> cls) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        for (Class<?> c : getClasses(cls)) {
            if (cls.isAssignableFrom(c) && !cls.equals(c)) {
                classes.add(c);
            }
        }
        return classes;
    }

    private static List<Class<?>> getClasses(Class<?> cls) throws ClassNotFoundException {
        String packageName = cls.getPackage().getName();
        String path = packageName.replace('.', '/');
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL url = classloader.getResource(path);
        return getClasses(new File(Objects.requireNonNull(url).getFile()), packageName);
    }

    private static List<Class<?>> getClasses(File dir, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!dir.exists()) {
            return classes;
        }
        for (File f : Objects.requireNonNull(dir.listFiles())) {
            if (f.isDirectory()) {
                classes.addAll(getClasses(f, packageName + "." + f.getName()));
            }
            String name = f.getName();
            if (name.endsWith(".class")) {
                classes.add(Class.forName(packageName + "." + name.substring(0, name.length() - 6)));
            }
        }
        return classes;
    }
}
