package com.itdom.abstractfactory;

import java.sql.Connection;

/**
 * 可以参考数据库连接java.sql.Connection
 */
public interface AbstractFactory {
public Screen createScreen();

public RemoteControl createRemoteControl();
}
