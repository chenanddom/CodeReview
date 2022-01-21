package com.itdom.graph;

import com.itdom.linear.Queue;

/**
 * 构造方法:
 * Digraph(int V)：创建一个包含V个顶点但不包含边的有向图
 * <p>
 * 成员方法
 * 1.public int V():获取图中顶点的数量
 * 2.public int E():获取图中边的数量
 * 3.public void addEdge(int v,int w):向有向图中添加一条边 v->w
 * 4.public Queue adj(int v)：获取由v指出的边所连接的所有顶点
 * 5.private Digraph reverse():该图的反向图
 * 成员变量
 * 1.private final int V: 记录顶点数量
 * 2.private int E: 记录边数量
 * 3.private Queue[] adj: 邻接表
 */
public class DiGraph {
    /**
     * 记录顶点的个数
     */
    private final int V;

    /**
     * 记录边的数量
     */
    private int E;

    /**
     * 邻接表
     */
    private Queue[] adj;

    public DiGraph(int V) {
        //舒适化顶点的个数
        this.V = V;
        //初始化边的条数
        this.E = 0;
        //初始化邻接表
        this.adj = new Queue[V];
        //初始化邻接表中的空队列
        for (int i = 0; i < V; i++) {
            adj[i] = new Queue<Integer>();
        }
    }

    /**
     * //获取顶点数目
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
     * 向有向图中添加一条边 v->w
     *
     * @param v
     * @param w
     */
    public void addEdge(int v, int w) {
        adj[v].enqueue(v);
        this.E++;
    }

    /**
     * 获取由v指出的边所连接的所有顶点
     *
     * @param v
     * @return
     */
    public Queue<Integer> adj(int v) {
        return adj[v];
    }

    /**
     * 该图的反向图
     */
    private DiGraph reverse() {
        DiGraph rDiGraph = new DiGraph(V);
        for (int v = 0; v < V; v++) {
            for (Integer w : adj(v)) {
                rDiGraph.addEdge(w, v);
            }
        }
        return rDiGraph;
    }

    public static void main(String[] args) {

    }
}
