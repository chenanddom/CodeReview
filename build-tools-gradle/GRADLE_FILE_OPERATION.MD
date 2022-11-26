## gradle文件操作
gradle文件操作使用的是Project.file(java.langg.Object)方法，通过指定文件的相对路径或者绝对路径来对文件操作，其中相对路径
为相对当当前的project的根project或者子project的目录。其实使用Project.file(java.lang.Object)方法创建的File对象就是Java中的
File对象，我们使用他就像使用Java中使用一样，

示例:

```groovy

//使用本地文件的操作方式:相对路径
File configFile = file("src/config.xml")
configFile.createNewFile()

//使用本地文件的操作方式:使用绝对路径
configFile = file("D:\\config.xml")
if (!configFile.exists()){
configFile.createNewFile()
}
//使用和Java中File的方式一样创建文件
configFile = new File('src/config_2.xml')
configFile.exists()?configFile.toString():configFile.createNewFile();

// 使用文件集合创建文件
FileCollection fileCollection = files("src/config_2.xml", ['src/config_3.xml', 'config_4.xml']);
fileCollection.forEach(item -> {
//    打印文件集合里面的文件的文件名
  println item.name
});
Set files = fileCollection.files
//files = fileCollection.files as Set
List fileList = fileCollection.files as List

def union = fileCollection + files('src/config_5.xml')

def minus = fileCollection - files('src/config_2.xml')

println "----------------------------------------------"
union.forEach(item -> {
//    打印文件集合里面的文件的文件名
  println item.name
});

println "----------------------------------------------"

minus.forEach(item -> {
//    打印文件集合里面的文件的文件名
  println item.name
});
println "----------------------------------------------"
```
### 文件树
文件数是有底层级结构的文件集合,一个文件数它可以代表一个目录结构或者一个ZIP压缩包中的内容结构。文件数是从文件集合继承过来的，所以文件数具有文件集合的所有功能
我们可以使用Project.fileTree(java.util.Map)方法来创建文件树对象，还可以使用过来条件来包含或者排除相关的文件。

示例:
```groovy
//普通方式创建文件树
FileTree fTree  = fileTree("src/main");
//闭包的方式表示文件树
fTree = fileTree('src/main'){
    include("**/*.java")
    exclude("**/*.txt")
}
//为参数赋值的方式创建文件树
fTree = fileTree(dir: "src/main",includes: ['**/*.java'],excludes: ['**/*.txt'])

fTree.forEach(item ->{
    println item.name;
})
```

### 文件拷贝
我们可以使用 Copy 任务来拷贝文件，通过它可以过虑指定拷贝内容，还能对文件进行重命名操作等。Copy 任务必须指定一组需要拷贝的文件和拷贝到的目录，
这里使用CopySpec.from(java.lang.Object[])方法指定原文件；使用CopySpec.into(java.lang.Object)方法指定目标目录


示例:
```groovy
// 拷贝文件，执行gradle+任务名
task copyTask(type: Copy){
    from "./src/"
    include("*.xml")
//    into './build/exploaderWar'
    exclude("config_2.xml")
//    重命名文件名字
    rename {String fileName->{
        fileName.replace("config","replace")
    }
    }
    into './src/main/resources'
}

//使用project.copy进行赋值,执行gradle build就会将文件赋值到指定的为止
copy {
    from file("./src/")
    include('*.xml')
    into this.buildDir.absolutePath
}


```


### 文件归档
通常一个项目会有很多的JAR包，我们希望把项目打包成一个WAR，ZIP或者TAR包进行发布，这个时候我们就可以使用Zip,Tar,Jar,War
和Ear任务来实现，不过他们的用法都是一样的，所哟我们指需要了解一个就够了。
```groovy
task compressPack(type: Zip){
    from 'src/main'
    into 'build'
    baseName ='myPack'
}
```






























































