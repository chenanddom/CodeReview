package com.itdom.graph;

import com.itdom.linear.Queue;

import java.util.Iterator;

/**
 * 图
 * Graph(int V)：创建一个包含V个顶点但不包含边的图
 * 成员方法:
 * 1.public int V():获取图中顶点的数量
 * 2.public int E():获取图中边的数量
 * 3.public void addEdge(int v,int w):向图中添加一条边 v-w
 * 4.public Queue adj(int v)：获取和顶点v相邻的所有顶点
 * 成员变量:
 * 1.private final int V: 记录顶点数量
 * 2.private int E: 记录边数量
 * 3.private Queue[] adj: 邻接表
 */
public class Graph {
    //记录顶点的个数
    private final int V;
    //记录表数量
    private int E;
    //邻接表
    private Queue<Integer>[] adj;

    public Graph(int v) {
        //初始化顶点的数量
        V = v;
        //初始化边的数量
        this.E = 0;
        //初始化邻接表
        this.adj = new Queue[v];
        //初始化邻接表中的空队列.
        for (int i = 0; i < adj.length; i++) {
            adj[i] = new Queue<Integer>();
        }
    }

    /**
     * 获取图中顶点的数量
     *
     * @return
     */
    public int V() {
        return V;
    }

    /**
     * 获取图中边的数量
     *
     * @return
     */
    public int E() {
        return E;
    }

    /**
     * 向图中添加一条边 v-w
     *
     * @param v
     * @param w
     */
    public void addEdge(int v, int w) {
        //把w添加到v队列中，这样顶点v就多了一个邻接点w
        adj[v].enqueue(w);
        //把v添加到w的链表中这样顶多w就多了一个相邻点v
        adj[w].enqueue(v);
        //边的数量+1
        E++;
    }

    /**
     * 获取和顶点v相邻的所有顶点
     *
     * @param v
     * @return
     */
    public Queue<Integer> adj(int v) {
        return adj[v];
    }

    public static void main(String[] args) {
        Graph graph = new Graph(5);
        graph.addEdge(4, 3);
        graph.addEdge(4, 2);
        graph.addEdge(4, 1);
        graph.addEdge(1, 2);
        graph.addEdge(0, 1);

        Queue adj = graph.adj(1);
        Iterator iterator = adj.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            System.out.println(next);
        }

    }

}
