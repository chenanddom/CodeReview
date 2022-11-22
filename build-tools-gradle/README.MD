






## 创建任务的方式
使用task方法创建
```groovy
task('A',{//任务名称,闭包都作为参数
 println "taskA..."
})
task('B'){//闭包作为最后一个参数可以直接从括号中拿出来
 println "taskB..."
}
task C{//groovy语法支持省略方法括号:上面三种本质是一种
 println "taskC..."
}
def map=new HashMap<String,Object>(); map.put("action",{println "taskD.."}) //action属性可以设置为闭包task(map,"D");
task(map,"D")
//是通过tasks 对象的 create创建任务
tasks.create("E"){
    println "taskE"
}
//注：register执行的是延迟创建。也即只有当task被需要使用的时候才会被创建。
tasks.register("F"){
    println "taskF"
}
```
### 创建任务的同时指定任务的属性

![创建任务的同时指定任务的属性](./files/task-parameter.jpeg)

* 示例:
```groovy
task("dp1"){
    doFirst {
        println "dp1"
    }
}
task("dp2"){
    doLast {
        println "dp2"
    }
}
//创建任务的时候指定属性
task dp3(group:"g1",dependsOn:["dp1","dp2"] ,action: { println "doAction"})
```

* 任务类型

| 常见任务类型                   | 该类型任务的作用                   |
|--------------------------|----------------------------|
| Delete                   | 删除文件或者目录                   |
| Copy                     | 将文件赋值到目标目录中 。此任务还可以在赋值时重命名 |
| CreateStartScripts       |创建启动脚本|
| Exec                     |执行命令行进程|
| GenerateMavenPom         |生成 Maven 模块描述符(POM)文件。|
| GradleBuild              |执行 Gradle 构建|
| Jar                      |组装 JAR 归档文件|
| JavaCompile              |编译 Java 源文件|
| Javadoc                  |为 Java 类 生 成 HTML API 文 档|
| PublishToMavenRepository |将 MavenPublication  发布到 mavenartifactrepostal。|
| Tar                      |组装 TAR 存档文件|
| Test                     |执行 JUnit (3.8.x、4.x 或 5.x)或 TestNG 测试。|
| Upload                   |将 Configuration 的构件上传到一组存储库。|
| War                      |组装 WAR 档案。|
| Zip                      |组装 ZIP 归档文件。默认是压缩 ZIP 的内容。|

提示 1：如果想看更详细的gradle 自带Task 类型，请参考官方文档: https://docs.gradle.org/current/dsl/index.html

* 示例:
```groovy
task taskType(type:Delete,group: "myDelete"){
    delete buildDir
}
```