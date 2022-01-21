[上一节](./TREE_ADVANCED.MD)
# 图
* 定义:
图是由一组顶带你和一组能够将两个顶点相连的边组成的。

![图](./files\graph-1.PNG)

* 特殊的图:
1. 自环: 即一条链接一个顶点和其滋生的边
2. 平行边:即链接同一对订单的两条婊

* 图的分类
按照链接两个顶点的边的而不同，可以把图分成一下两种:
无向图:边仅仅链接两个顶点,没有其他含义;
有向图:边不仅链接两个顶点，并且具有方向;



## 无向图

* 相邻顶点:

当两个顶带你通过一条边相连时，我们称这两个顶点是相邻的，并且称这条边依附于这两个顶点.

* 度:

某个顶点的度就是依附于该顶点的边的条数

* 子图:

是一幅图的所有边的子集(包含这些边依附的顶点)组成的图

* 路径:

是由边顺序链接的一系列的顶点组成.

* 环

是一条至少含有一条边且终点和七点相同的路径.

* 连通图:
 如果图中任意一个顶点都存在一条路径达到另外一个顶点，那么这幅图就成为连通图
 
* 连通子图:
一个非连通图由若干连通的部分组成，每一个连通的部分都可以成为该图的连通子图


* 图的存储结构
要表示一幅图，只需要表示清楚以下两个部分的内容即可:

    1. 图中所有的顶点
    2. 所有连接顶点的边 

常见的图的存储结构由两种:邻接矩阵和邻接表

* 邻接矩阵
    1. 使用一个V*V的二维数组int[V][V] adj，把索引的值看做四顶点
    2. 如果顶点v和顶点w相连，我们只需要将adj[v][w]和adj[w][v]的值社子和为1，的否则设置为0即可.
 
![邻接矩阵](./files\graph-1.PNG)    

结论: 很明显，邻接矩阵这种存储方式的空间复杂度是V^2的，如果我们处理的问题规模比较大的话，内存空间极有可能不够用。

* 邻接表

1. 使用一个大小为V的数组 Queue[V] adj,把索引看做是顶点.
2. 每个索引处adj[V]存储一个队列,该队列中存储的是所有与该顶点相邻的的其他顶点.

![邻接矩阵](./files\graph-3.PNG)    

结论: 灵界表的空间并不是线性级别的，所有后面我们一直才有临界表这种存储形式来表示图.

## 图的实现
```java
public class Graph {
    //记录顶点的个数
    private final int V;
    //记录表数量
    private int E;
    //邻接表
    private Queue[] adj;

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
    public Queue adj(int v) {
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
```

## 图的遍历
很多情况下，我们需要遍历图，得到图的一些性质，例如，找出图中与指定的顶点相连的所有顶点，或者判断某个顶点与指定顶点事都相通，是非常常见的需求。
图的搜索常用的有深度优先搜索和广度优先搜索。

* 深度优先搜索
所谓的深度优先搜索，指的是在搜索时，如果遇到一个结点既有子结点，又有兄弟结点，那么优先找子结点，然后找兄弟结点。
![深度优先搜索](./files\graph-4.PNG)

结论:
很明显，在由于边是没有方向的，所以，如果4和5顶点相连，那么4会出现在5的相邻链表中，5也会出现在4的相
邻链表中，那么为了不对顶点进行重复搜索，应该要有相应的标记来表示当前顶点有没有搜索过，可以使用一个布
尔类型的数组 boolean[V] marked,索引代表顶点，值代表当前顶点是否已经搜索，如果已经搜索，标记为true，
如果没有搜索，标记为false；


* 深度优先搜索



* 广度优先搜索





# 图的进阶
## 有向图的定义
* 有向图是一副具有方向性的图，是由一组顶点和一组有方向的边组成的，每条方向的边都连着一对游学的顶点

* 出度:
由某个顶点指出的边的个数成为改顶点的出度

* 入度
指向某个顶点的边的个数成为该顶带你的入度

* 有向路径
由一些列的顶点组成，对于其中的每个顶点都存在一条有向边，从它指向序列的下一个顶点。

* 有向环
一条至少含有一条边，且起点和重点相同的有向路径。

* 一副有向图中两个顶带你v和w可以存在以下的4中关系:
1. 没有边相连
2. 存在从v到w的边v->w
3. 存在w到v的边w->v
4. 既存在w到v的边，也存在v到w的边，，即双向连接。

**有向图代码的实现:**
```java
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
        //创建一个新的有向图
        DiGraph rDiGraph = new DiGraph(V);
        //遍历与v相关的顶点的邻接表
        for (int v = 0; v < V; v++) {
            for (Integer w : adj(v)) {
                //添加邻接表的v顶点的边。
                rDiGraph.addEdge(w, v);
            }
        }
        return rDiGraph;
    }
}
```
### 检测有向图中的环
```text

在进行图的拓扑排序时，需要保证图中是没有环的

```















































