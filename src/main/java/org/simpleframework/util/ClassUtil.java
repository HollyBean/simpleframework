package org.simpleframework.util;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class ClassUtil {

    public static final String FILE_PROTOCOL = "file";

    /**
     * 传入包名，自动扫描加载该目录下的class文件
     * @param packageName eg:com.hollybean
     * @return Set<Class<?>>
     */
    public static Set<Class<?>> extractPackageClassSet(String packageName) {
        // 1.获取类加载器
        ClassLoader classLoader = getClassLoader();
        // 2.定位资源
        String resourceName = packageName.replace(".", "/");
        URL url = classLoader.getResource(resourceName);
        if (url == null) {
            throw new RuntimeException("无法定位包体位置:" + packageName);
        }
        Set<Class<?>> result = new HashSet<>();
        // 3.根据资源类型进行处理
        if (FILE_PROTOCOL.equalsIgnoreCase(url.getProtocol())) {
            // 4.递归扫描
            File directory = new File(url.getPath());
            extractDirectoryClassSet(directory, result, packageName);
        } else {
            // 暂时只支持file类型文件
            throw new RuntimeException("不支持文件类型：" + url.getProtocol());
        }
        return result;
    }

    private static void extractDirectoryClassSet(File file, Set<Class<?>> result, String packageName) {
        if (!file.isDirectory()) {
            return ;
        }
        // 筛选目录
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File path) {
                if (path.isDirectory()) {
                    return true;
                } else {
                    // 如果是非目录，并且以".class"结尾的文件则生成对应的Class实例，并添加到结果集
                    // 需要注意，文件名我们还是需要进一步处理的
                    String classPath = path.getAbsolutePath().replace(File.separator, ".");
                    if (classPath.endsWith(".class")) {
                        // 最终我们需要得到"com.hollybean.controller.HelloController"的格式，所以需要去头去尾
                        classPath = classPath.substring(classPath.indexOf(packageName));
                        classPath = classPath.substring(0, classPath.lastIndexOf("."));
                        addToClassSet(classPath);
                    }
                }
                return false;
            }

            private void addToClassSet(String classPath) {
                try {
                    Class<?> clazz = Class.forName(classPath);
                    result.add(clazz);
                } catch (Throwable t) {
                    throw new RuntimeException(t);
                }
            }
        });
        // 需要注意foreach遍历时，字节码其实还是用的对象iterator，所以必须判空
        if (files != null) {
            for (File directory : files) {
                extractDirectoryClassSet(directory, result, packageName);
            }
        }
    }

    private static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 简单实现利用无参构造函数返回实例
     * @param clazz Class实例
     * @param accessible 设置可见性
     * @param <T> 泛型转换
     * @return
     */
    public static <T> T getBean(Class<?> clazz, boolean accessible) throws  Throwable{
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(accessible);
        return (T)constructor.newInstance();
    }
}
