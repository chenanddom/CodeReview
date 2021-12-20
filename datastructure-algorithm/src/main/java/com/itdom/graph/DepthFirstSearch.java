package com.itdom.graph;

import com.itdom.linear.Queue;

/**
 * 深度优先搜索
 * 构造方法:
 * DepthFirstSearch(Graph G,int s)：构造深度优先搜索对象，使用深度优先搜索找出G图中s顶点
 * 的所有相通顶点
 * 成员方法:
 * 1.private void dfs(Graph G, int v)：使用深度优先搜索找出G图中v顶点的所有相通顶点
 * 2.public boolean marked(int w):判断w顶点与s顶点是否相通
 * 3.public int count():获取与顶点s相通的所有顶点的总数
 * 成员变量:
 * 1.private boolean[] marked: 索引代表顶点，值表示当前顶点是否已经被搜索
 * 2.private int count：记录有多少个顶点与s顶点相通
 */
public class DepthFirstSearch {
    //索引代表了顶点，值表示当前顶点是否已经被搜索
    private boolean[] marked;
    //记录有多少个顶多与S顶点相通.
    private int count;

    public DepthFirstSearch(Graph graph,int s) {
        //创建一个和图顶带你树一样大的布尔数组。
        marked = new boolean[graph.V()];
        //搜索g图与顶点相通的所有顶点。
        dfs(graph,s);

    }

    /**
     * 使用深度优先搜索找出G图中v顶点的所有相通顶点
     * @param graph
     * @param v
     */
    private void dfs(Graph graph, int v){
        //把当前结点的标记为已经搜索
        marked[v]=true;
        //遍历v顶点的邻接表，得到每一个顶点
        Queue<Integer> adj = graph.adj(v);
        for (Integer  w:adj) {
            if (!marked[w]){
                dfs(graph,w);
            }
        }
        count++;
    }

    /**
     * 判断w顶点与s顶点是否相通
     * @param w
     * @return
     */
    public boolean marked(int w){
        return marked[w];
    }
    public int count(){
        return count;
    }

    public static void main(String[] args) {
        //准备Graph对象
        Graph graph = new Graph(13);
       graph.addEdge(0,5);
       graph.addEdge(0,1);
       graph.addEdge(0,2);
       graph.addEdge(0,6);
       graph.addEdge(5,3);
       graph.addEdge(5,4);
       graph.addEdge(3,4);
       graph.addEdge(4,6);

       graph.addEdge(7,8);

       graph.addEdge(9,11);
       graph.addEdge(9,10);
       graph.addEdge(9,12);
       graph.addEdge(11,12);
        DepthFirstSearch depthFirstSearch = new DepthFirstSearch(graph, 0);
        int count = depthFirstSearch.count();
        System.out.println("与起点0相通的顶点的数量:"+count);
        //测试某个顶点与起点是否相同
        boolean marked1 = depthFirstSearch.marked(5);
        System.out.println("顶点5和顶点0是否相通："+marked1);
        boolean marked2 = depthFirstSearch.marked(7);
        System.out.println("顶点7和顶点0是否相通："+marked2);

    }
}
