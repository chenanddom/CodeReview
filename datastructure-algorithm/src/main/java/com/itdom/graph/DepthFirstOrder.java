package com.itdom.graph;

import com.itdom.linear.Stack;

/**
 * 基于深度优先的顶点排序
 */
public class DepthFirstOrder {
    /**
     * 索引代表顶点，值表示当前顶点是否已经被搜索
     */
    private boolean[] marked;
    /**
     * 使用栈，存储顶点的序列
     */
    private Stack<Integer> reversePost;


    public DepthFirstOrder(DiGraph graph) {
        //创建一个和图的顶点数一样大小的marked数组
        this.marked = new boolean[graph.V()];
        this.reversePost = new Stack<Integer>();
         //遍历搜搜哦图中的每个顶点
        for (int v = 0; v < graph.V(); v++) {
            //如果当前顶点没有被搜索过，就搜索
            if (!marked[v]) {
                dfs(graph, v);
            }
        }
    }

    //基于深度优先搜索的，检测图graph中是否有环
    private void dfs(DiGraph graph, int v) {
        //标记搜索的顶点已经被搜索
        marked[v]=true;
        //获取和v顶点相关的邻接表
        for (Integer w : graph.adj(v)) {
            if (!marked[w]) {
                dfs(graph, w);
            }
        }
        //搜索完将顶点放入栈中
        reversePost.push(v);
    }

    public Stack reversePost() {
        return reversePost;
    }


    public static void main(String[] args) {

    }
}
