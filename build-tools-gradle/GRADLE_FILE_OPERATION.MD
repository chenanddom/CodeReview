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
