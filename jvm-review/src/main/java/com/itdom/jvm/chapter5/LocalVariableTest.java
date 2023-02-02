package com.itdom.jvm.chapter5;

/**
 * 测试局部变量需要手动赋值，否则无法正常使用的场景
 */
public class LocalVariableTest {
public void localVariableMethod(){
    int a = 0;//此处不赋值无法正常使用。
    System.out.println(a);
}
}
