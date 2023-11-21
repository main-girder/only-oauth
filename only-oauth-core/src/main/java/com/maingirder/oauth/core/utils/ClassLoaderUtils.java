package com.maingirder.oauth.core.utils;

import lombok.SneakyThrows;

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
     * @param clazz
     * @return
     */
    public static List<Class<?>> getListByClass(Class<?> clazz) {
        List<Class<?>> classes = new ArrayList<>();
        for (Class<?> c : getClasses(clazz)) {
            if (clazz.isAssignableFrom(c) && !clazz.equals(c)) {
                classes.add(c);
            }
        }
        return classes;
    }


    private static List<Class<?>> getClasses(Class<?> clazz) {
        String packageName = clazz.getPackage().getName();
        String path = packageName.replace('.', '/');
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL url = classloader.getResource(path);
        return getClasses(new File(Objects.requireNonNull(url).getFile()), packageName);
    }

    /**
     * 根据目录以及包名获取class列表
     * todo @SneakyThrows 临时使用后期替换
     *
     * @param dir
     * @param packageName
     * @return
     */
    @SneakyThrows
    private static List<Class<?>> getClasses(File dir, String packageName) {
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
