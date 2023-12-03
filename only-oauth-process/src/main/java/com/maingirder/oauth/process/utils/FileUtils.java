package com.maingirder.oauth.process.utils;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * FileUtils
 *
 * @author jason-guo-cr
 * @version 1.0.0
 * @since 2023/12/3 10:04
 */
public class FileUtils {

    private static final int DEPTH = 10;

    private static final List<String> BUILD_FILE_LIST = Arrays.asList("pom.xml", "build.gradle");


    /**
     * 寻找项目根目录
     *
     * @param file file
     * @return 根目录
     */
    public static String findProjectRoot(File file) {
        Path currentPath = file.toPath();
        Optional<Path> rootPath = findRootPath(currentPath);

        return rootPath.map(Path::toString).orElse(null);
    }

    /**
     * 寻找项目根目录
     *
     * @param path path
     * @return 根目录
     */
    private static Optional<Path> findRootPath(Path path) {
        for (int i = 0; i < DEPTH; i++) {
            if (path == null) {
                return Optional.empty();
            }

            File[] files = path.toFile().listFiles();
            if (containsBuildFile(files)) {
                Path parentPath = path.getParent();
                if (parentPath == null || !containsBuildFile(parentPath.toFile().listFiles())) {
                    return Optional.of(path);
                }
            }
            path = path.getParent();
        }
        return Optional.empty();
    }

    /**
     * 是否包含构建文件
     *
     * @param files 文件列表
     * @return true/false
     */
    private static boolean containsBuildFile(File[] files) {
        if (files != null) {
            for (File file : files) {
                if (BUILD_FILE_LIST.contains(file.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

}
