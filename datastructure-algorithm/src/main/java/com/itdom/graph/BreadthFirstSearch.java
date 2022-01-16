package com.itdom.graph;

import com.itdom.linear.Queue;

/**
 * 广度优先搜索
 * 构造方法:
 * BreadthFirstSearch(Graph G,int s)：构造广度优先搜索对象，使用广度优先搜索找出G图中s顶
 * 点的所有相邻顶点
 * 成员变量:
 * 1.private boolean[] marked: 索引代表顶点，值表示当前顶点是否已经被搜索
 * 2.private int count：记录有多少个顶点与s顶点相通
 * 3.private Queue waitSearch: 用来存储待搜索邻接表的点
 * 成员方法:
 * 1.private void bfs(Graph G, int v)：使用广度优先搜索找出G图中v顶点的所有相邻顶点
 * 2.public boolean marked(int w):判断w顶点与s顶点是否相通
 * 3.public int count():获取与顶点s相通的所有顶点的总数
 */
public class BreadthFirstSearch {
    //索引代表顶点，值表示当前顶点是否已经被搜索
    private boolean[] marked;
    //记录有多少个顶点与s顶点相通
    private int count;
    //用来存储待搜索邻接表的点
    private Queue<Integer> waitSearch;

    public BreadthFirstSearch(Graph graph, int s) {
        //创建一个和图顶点数量大小一样的布尔数组
        marked = new boolean[graph.V()];
        //初始化队列的
        waitSearch = new Queue<Integer>();
        //搜索G图中与顶点S相通的所有顶点
        dfs(graph, s);
    }

    /**
     * 使用广度优先搜索找出G图中v顶点的所有相邻顶点
     *
     * @param graph
     * @param v
     */
    private void dfs(Graph graph, int v) {
        //把当前的顶点v标记为已搜索
//        marked[v] = true;
        //把当前顶点v放入对抗i额中，等待搜索它的邻接表
//        waitSearch.enqueue(v);
        //使用while循环从队列涨拿出待搜索的顶带你wait，进行搜索邻接表
//        while (!waitSearch.isEmpty()) {
//            Integer wait = waitSearch.dequeue();
        //遍历wait顶点的邻接表,得到每一个顶点w
//            Queue<Integer> adj = graph.adj(wait);
//            for (Integer w : adj) {
//                if (!marked[w]) {
//                    dfs(graph, w);
//                }
//            }
//        }
//        count++;
        marked[v] = true;
        waitSearch.enqueue(v);
        while (!waitSearch.isEmpty()) {
            Integer wait = waitSearch.dequeue();
            Queue<Integer> adj = graph.adj(wait);
            for (Integer w : adj) {
                if (!marked[w]) {
                    marked[w] = true;
                    waitSearch.enqueue(w);
                    this.count++;
                }
            }
        }
    }


    /**
     * 判断w顶点与s顶点是否相通
     *
     * @param w
     * @return
     */
    public boolean marked(int w) {
        return marked[w];
    }

    public int count() {
        return count;
    }


    public static void main(String[] args) {
        //准备Graph对象
        Graph graph = new Graph(13);
        graph.addEdge(0, 5);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 6);
        graph.addEdge(5, 3);
        graph.addEdge(5, 4);
        graph.addEdge(3, 4);
        graph.addEdge(4, 6);

        graph.addEdge(7, 8);

        graph.addEdge(9, 11);
        graph.addEdge(9, 10);
        graph.addEdge(9, 12);
        graph.addEdge(11, 12);
        BreadthFirstSearch breadthFirstSearch = new BreadthFirstSearch(graph, 0);

        int count = breadthFirstSearch.count();
        System.out.println("与0结点相通的结点数:" + count);
        //测试某个顶点与起点是否相同
        boolean marked1 = breadthFirstSearch.marked(5);
        System.out.println("顶点5和顶点0是否相通：" + marked1);
        boolean marked2 = breadthFirstSearch.marked(7);
        System.out.println("顶点7和顶点0是否相通：" + marked2);
    }
}
