# Gradle插件

* 使用插件的原因
简单的说,通过应用插件我们可以:
1. 促进代码重用、减少功能类似代码编写、提升工作效率
2. 促进项目更高程度的模块化、自动化、便捷化
3. 可插拔式的的扩展项目的功能

* 插件的作用
  在项目构建过程中做很多事情，把插件应用到项目中，通常可以完成:
  1、可以添加任务【task】到项目中，从而帮助完成测试、编译、打包等。
  2、可以添加依赖配置到项目中。
  3、可以向项目中拓展新的扩展属性、方法等。
  4、可以对项目进行一些约定，如应用 Java 插件后，约定src/main/java 目录是我们的源代码存在位置，编译时编译这个目录下的Java 源代码文件。


## 插件的分类和使用

![插件的分类和使用](./files/gradle-plugin-1.jpeg)

* 脚本插件
  脚本插件的本质就是一个脚本文件，使用脚本插件时通过apply from:将脚本加载进来就可以了，后面的脚本文件可以是本地的也可以是网络上的脚本文件， 
  下面定义一段脚本，我们在 build.gradle 文件中使用它，具体如下：

例子:
```groovy
//config.gradle文件内容如下:
ext{
  company= "尚硅谷"
  cfgs = [
          compileSdkVersion : JavaVersion.VERSION_1_8
  ]
  spring = [
          version : '5.0.0'
  ]
}

//build.gradle引入
apply from:"config.gradle"
//在build.gradle引入之后就相当于在本文将的内容一样可以正常的使用
task taskVersion(){
  doLast {
    println "公司名称为：${company},JDK版本是${cfgs.compileSdkVersion},版本号是${spring.version}"
  }
}

//执行的结果：公司名称为：尚硅谷,JDK版本是1.8,版本号是5.0.0
```