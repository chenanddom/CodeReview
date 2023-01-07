package com.itdom.jvm.chapter2;

public class ClassLoaderTest {
    public static void main(String[] args) {
        //获取系统类加载器
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
//        两次获取系统类加载器的值都相同：sun.misc.Launcher$AppClassLoader@18b4aac2 ，这说明系统类加载器是全局唯一的
        System.out.println(systemClassLoader);//sun.misc.Launcher$AppClassLoader@18b4aac2
        //获取其上层:扩展类加载器
        ClassLoader extClassLoader = systemClassLoader.getParent();
        System.out.println(extClassLoader);//sun.misc.Launcher$ExtClassLoader@38af3868
        //获取其上层：获取不到引导类加载器
        ClassLoader bootStrapClassLoader = extClassLoader.getParent();
//        我们尝试获取引导类加载器，获取到的值为 null ，这并不代表引导类加载器不存在，因为引导类加载器右 C/C++ 语言，我们获取不到
        System.out.println(bootStrapClassLoader);//null
        //对于用户自定义类来说:默认使用系统类加载其进行加载
        ClassLoader classLoader = ClassLoaderTest.class.getClassLoader();
//        两次获取系统类加载器的值都相同：sun.misc.Launcher$AppClassLoader@18b4aac2 ，这说明系统类加载器是全局唯一的
        System.out.println(classLoader);//sun.misc.Launcher$AppClassLoader@18b4aac2
        //String类使用引导类加载器进行加载的，
        ClassLoader classLoader1 = String.class.getClassLoader();
        System.out.println(classLoader1);//null
    }
}
