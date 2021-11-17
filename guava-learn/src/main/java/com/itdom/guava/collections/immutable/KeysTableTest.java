package com.itdom.guava.collections.immutable;

import com.google.common.collect.HashBasedTable;

public class KeysTableTest {
    public static void main(String[] args) {
        HashBasedTable<Object, Object, Object> hashBasedTable = HashBasedTable.create();
        hashBasedTable.put("zhangsan","key1",91);
        hashBasedTable.put("zhangsan","key2",95);
        hashBasedTable.put("zhangsan","key3",98);

        hashBasedTable.put("lisi","key1",88);
        hashBasedTable.put("lisi","key2",91);
        hashBasedTable.put("lisi","key3",92);

        System.out.println(hashBasedTable.get("zhangsan","key1"));
        System.out.println(hashBasedTable.get("lisi","key1"));

    }
}
