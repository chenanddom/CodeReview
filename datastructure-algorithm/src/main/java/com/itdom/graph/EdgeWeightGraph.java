package com.itdom.graph;

import com.itdom.linear.Queue;

/**
 * 加权无向图的实现
 */
public class EdgeWeightGraph {
    /**
     * 记录顶点的数量
     */
    private final int V;
    /**
     * 记录边的数量
     */
    private int E;
    /**
     * 邻接表
     */
    private Queue<Edge>[] adj;


    /**
     * 创建一个含有V个顶带你的空加权无向图
     *
     * @param v
     */
    public EdgeWeightGraph(int v) {
        this.V = v;
        this.E = 0;
        this.adj = new Queue[v];
        for (int i = 0; i < adj.length; i++) {
            adj[i] = new Queue<Edge>();
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
     * 获取图中的边的数量
     *
     * @return
     */
    public int E() {
        return E;
    }

    /**
     * 向加权无向图添加一条边
     *
     * @param edge
     */
    public void addEdge(Edge edge) {
        int v = edge.either();
        int w = edge.other(v);
        adj[v].enqueue(edge);
        adj[w].enqueue(edge);
        this.E++;
    }

    /**
     * 获取和顶点v关联的所有边
     *
     * @param v
     * @return
     */
    public Queue<Edge> adj(int v) {
        return adj[v];
    }

    /**
     * 获取加权无向图的所有边
     *
     * @return
     */
    public Queue<Edge> edges() {
        Queue<Edge> edges = new Queue<>();
        //遍历图中的每一个顶点，找到该顶带你的邻接表，邻接表中存储了该顶点关联的每一条表

        //无向图，关联的两个顶点都贵同时记录同一条边
        for (int v = 0; v < V; v++) {
            for (Edge edge : adj(v)) {
                if (edge.other(v) < v) {
                    edges.enqueue(edge);
                }
            }
        }
        return edges;
    }
}
