package com.itdom.graph;

import com.itdom.linear.Stack;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TopoLogical {

    //顶点的拓扑排序
    private Stack<Integer> order;

    public TopoLogical(DiGraph graph) {
        //判断给定的图是否有环
        DirectedCycle directedCycle = new DirectedCycle(graph);
        if (!directedCycle.hasCycle()){
            DepthFirstOrder depthFirstOrder = new DepthFirstOrder(graph);
            this.order = depthFirstOrder.reversePost();
        }
    }
    //帕努但图graph是否有环
    private  boolean isCycle(){
        return order==null;
    }
    //获取拓扑排序的所有顶点
    public Stack<Integer> order(){
        return order;
    }

    public static void main(String[] args) {
        //准备有向图
        DiGraph diGraph = new DiGraph(6);
        diGraph.addEdge(0,2);
        diGraph.addEdge(0,3);
        diGraph.addEdge(2,4);
        diGraph.addEdge(3,4);
        diGraph.addEdge(4,5);
        diGraph.addEdge(1,3);
        //通过topologica对有向图进行排序
        TopoLogical topoLogical = new TopoLogical(diGraph);
        //打印顶点的序列
        Stack<Integer> order = topoLogical.order;
        StringBuilder sb = new StringBuilder();
        for (Integer w : order) {
            sb.append(w+"->");
        }
        int index = sb.toString().lastIndexOf("->");
        String substring = sb.toString().substring(0, index);
        System.out.println(substring);
    }

}
