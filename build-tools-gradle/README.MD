






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

