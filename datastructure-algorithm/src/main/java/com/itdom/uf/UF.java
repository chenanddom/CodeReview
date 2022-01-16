package com.itdom.uf;

import java.util.Scanner;

/**
 * 构造方法UF(int N)：初始化并查集，以整数标识(0,N-1)个结点
 * <p>
 * <p>
 * 成员方法
 * 1.public int count()：获取当前并查集中的数据有多少个分组
 * 2.public boolean connected(int p,int q):判断并查集中元素p和元素q是否在同一分组中
 * 3.public int find(int p):元素p所在分组的标识符
 * 4.public void union(int p,int q)：把p元素所在分组和q元素所在分组合并
 * 成员变量
 * 1.private int[] eleAndGroup: 记录结点元素和该元素所在分组的标识
 * 2.private int count：记录并查集中数据的分组个数
 */
public class UF {
    /**
     * 记录结点元素和该元素所在分组的标识
     */
    private int[] eleAndGroup;
    /**
     * 记录并查集中数据的分组个数
     */
    private int count;

    public UF(int N) {
        this.eleAndGroup = new int[N];
        this.count = N;
        for (int i = 0; i < N; i++) {
            this.eleAndGroup[i] = i;
        }
    }

    /**
     * 获取当前并查集中的数据有多少个分组
     */
    public int count() {
        return this.count;
    }

    /**
     * 判断并查集中元素p和元素q是否在同一分组中
     */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    /**
     * 元素p所在分组的标识符
     */
    public int find(int p) {
        return this.eleAndGroup[p];
    }

    /**
     * 把p元素所在分组和q元素所在分组合并
     */
    public void union(int p, int q) {
        if (connected(p, q)) {
            return;
        }
        int pGroup = find(p);
        int qGroup = find(q);
        for (int i = 0; i < this.count; i++) {
            if (this.eleAndGroup[i] == pGroup) {
                this.eleAndGroup[i] = qGroup;
            }
        }
        this.count--;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请录入并查集中元素的个数");
        UF uf = new UF(scanner.nextInt());
        while (true) {
            System.out.println("请录入您要合并的第一个点:");
            int p = scanner.nextInt();
            System.out.println("请录入您要合并的第二个点:");
            int q = scanner.nextInt();
            //判断p和q是否在同一个组
            if (uf.connected(p, q)) {
                System.out.println("结点：" + p + "结点" + q + "已经在同一个组");
                continue;
            }
            uf.union(p, q);
            System.out.println("总共还有" + uf.count() + "个分组");
        }
    }
}
