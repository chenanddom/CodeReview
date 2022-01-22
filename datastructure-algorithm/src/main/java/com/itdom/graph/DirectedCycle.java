package com.itdom.graph;

import com.itdom.linear.Queue;

/**
 * 构造方法:
 * DirectedCycle(Digraph G)：创建一个检测环对象，检测图G中是否有环
 * <p>
 * 成员方法:
 * 1.private void dfs(Digraph G,int v)：基于深度优先搜索，检测图G中是否有环
 * 2.public boolean hasCycle():判断图中是否有环
 * <p>
 * 成员变量:
 * 1.private boolean[] marked: 索引代表顶点，值表示当前顶点是否已经被搜索
 * 2.private boolean hasCycle: 记录图中是否有环
 * 3.private boolean[] onStack:索引代表顶点，使用栈的思想，记录当前顶点有没有已经处于正在
 * 搜索的有向路径上
 *
 */
public class DirectedCycle {
    /**
     * 索引代表顶点，值表示当前顶点是否已经被搜索
     */
    private boolean[] marked;
    /**
     * 记录图中是否有环
     */
    private boolean hasCycle;
    /**
     * 索引代表顶点，使用栈的思想，记录当前顶点有没有已经处于正在
     */
    private boolean[] onStack;

    public DirectedCycle(DiGraph diGraph) {
        this.marked = new boolean[diGraph.V()];
        this.hasCycle = false;
        this.onStack = new boolean[diGraph.V()];
        for (int v = 0; v < diGraph.V(); v++) {
            if (!marked[v]) {
                dfs(diGraph, v);
            }
        }
    }

    /**
     * 基于深度优先搜索，检测图G中是否有环
     *
     * @param graph
     * @param v
     */
    private void dfs(DiGraph graph, int v) {
        //将当前的顶点标记为已搜索
        marked[v] = true;
        //栈同样设置为true表示当前顶点进栈
        onStack[v] = true;
        for (Integer w : graph.adj(v)) {
            if (!marked[w]) {
                dfs(graph, w);
            }
            if (onStack[w]) {
                hasCycle = true;
                return;
            }
        }
        //如果深度遍历完当前顶点的邻接表的所顶点都没有环，那么当见当前顶点出栈。
        onStack[v] = false;
    }

    /**
     * 判断图中是否有环
     */
    public boolean hasCycle() {
        return hasCycle;
    }


    public static void main(String[] args) {

    }
}
