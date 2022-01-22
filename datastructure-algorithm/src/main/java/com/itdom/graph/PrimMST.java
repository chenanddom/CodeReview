package com.itdom.graph;

import com.itdom.linear.Queue;
import com.itdom.priorityqueue.IndexMinPriorityQueue;
import com.itdom.priorityqueue.IndexMinPriorityQueue2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * prim算法实现最小生成树
 */
public class PrimMST {
    //索引代表顶点，值表示当前顶点和最小生成树之间的最短边
    private Edge[] edgeTo;
    //索引代表顶点，值表示当前顶点和最小生成树之间的最短边的权重
    private double[] distTo;
    //索引代表顶点，如果当前顶带你已经在数中，则值为true，否则为false;
    private boolean[] marked;
    //存储树中顶带你与非树中顶点之间的有效横切边
    private IndexMinPriorityQueue2<Double> pq;

    //根据一副加权无向图，创建最小生成树计算对象
    public PrimMST(EdgeWeightGraph graph) {
        this.edgeTo = new Edge[graph.V()];
        this.distTo = new double[graph.V()];
        for (int i = 0; i < this.distTo.length; i++) {
            distTo[i] = Double.POSITIVE_INFINITY;
        }

        this.marked = new boolean[graph.V()];
        this.pq = new IndexMinPriorityQueue2<Double>(graph.V());
        //默认让顶点0进入倒树中，但是树中只有一个顶点0，因此，0垫高点默认没有和其他的顶点相连，所以让distTo对应位置处的值存储0.0;
        this.distTo[0] = 0.0;
        pq.insert(0, 0.0);
        //遍历索引最小优先队列，拿到最小横切边的对应的顶点，把该顶点加入倒最小生成树中
        while (!pq.isEmpty()) {
            visit(graph, pq.delMin());
        }
    }

    /**
     * 将顶点v添加最小生成树中，并且更新数据
     *
     * @param graph
     * @param v
     */
    private void visit(EdgeWeightGraph graph, int v) {
        //把顶点v添加倒最小生成树中。
        marked[v] = true;
        //更新数据
        for (Edge edge : graph.adj(v)) {
            int w = edge.other(v);
            //判断另外一个顶带你是不是已经存在书中，如果在书中，则不做任何处理，如果不在树中，更新数据
            if (marked[w]) {
                continue;
            }
            //判断edge边权重是否小于从w顶带你倒树中已经存在的最小边的权重
            if (edge.weight() < distTo[w]) {
                edgeTo[w] = edge;
                distTo[w] = edge.weight();
                if (pq.contains(w)) {
                    pq.changeItem(w, edge.weight());
                } else {
                    pq.insert(w, edge.weight());
                }
            }


        }

    }

    /**
     * 获取最小生成树的所有边
     *
     * @return
     */
    public Queue<Edge> edges() {
        //创建队列对象
        Queue<Edge> edges = new Queue<>();
        //遍历edgeTo数组，拿到每一条边，如果不为空,顺序添加倒队列重
        for (int i = 0; i < edgeTo.length; i++) {
            if (edgeTo[i]!=null){
                edges.enqueue(edgeTo[i]);
            }
        }
        return edges;
    }

    public static void main(String[] args) throws IOException {
        //准备加权无向图
        BufferedReader reader = new BufferedReader(new InputStreamReader(PrimMST.class.getResourceAsStream("min_create_tree_test.txt")));
        int total = Integer.parseInt(reader.readLine());
        EdgeWeightGraph weightGraph = new EdgeWeightGraph(total);
        int edgeNumbers = Integer.parseInt(reader.readLine());
        for (int e = 1; e <=edgeNumbers ; e++) {
            String line = reader.readLine();
            String[] strs = line.split(" ");
            int v = Integer.parseInt(strs[0]);
            int w = Integer.parseInt(strs[1]);
            double weight = Double.parseDouble(strs[2]);
            //构建加权无向边
            Edge edge = new Edge(v, w, weight);
            weightGraph.addEdge(edge);
        }
        //创建一个PrimMST对象，计算加权无向图的最小生成树
        PrimMST primMST = new PrimMST(weightGraph);
        //获取最小生成树的所有边
        Queue<Edge> edges = primMST.edges();

        //遍历打印所有的边
        for (Edge edge : edges) {
            int v = edge.either();
            int w = edge.other(v);
            double weight = edge.weight();
            System.out.println(v+"-"+w+"::"+weight);
        }
    }

}
