# 依赖的方式

## 本地依赖

依赖本地的某个jar包，具体可以通过 文件集合、文件树的方式指定

## 项目依赖

项目依赖，依赖某个project

## 直接依赖

依赖的类型 依赖的组名 依赖的名称 依赖的版本号

```groovy
dependencies {
//本地依赖，使用文件集合或者文件树的方式实现依赖
//    implementation files("./lib/commons-lang-2.6.jar")
//    implementation fileTree(dir: './lib',includes: ['*.jar'])
//项目依赖
//    implementation project(":projectname")
//直接依赖，就是在maven仓库找到依赖的作保直接放入即可
// https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation group: 'com.google.code.gson', name: 'gson', version: '2.10'

}
```