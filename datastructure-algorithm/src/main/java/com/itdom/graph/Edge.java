package com.itdom.graph;

/**
 * 加权无向图
 */
public class Edge implements Comparable<Edge> {
    //顶点1
    private final int v;
    //顶点2
    private final int w;
    //当前边的权重
    private final double weight;

    public Edge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }
    //获取边的权重值
    public double weight(){
        return this.weight;
    }
    //获取边上的一个点
    public int either(){
        return v;
    }
    //获取边上除了顶点vertex外的另外一个顶点
    public int other(int vertex){
       return vertex==this.v?this.w:this.v;
    }

    @Override
    public int compareTo(Edge o) {
        int cmp;
        //如果当前边的权重值大，则cmp=1
        if (this.weight>o.weight){
            cmp=1;
        }else if (this.weight<o.weight) {
            //如果当前边的权重值小，则cmp=-1
            cmp=-1;
        }else {
            //如果当前边的权重值和o一样大，则cmp=0
        cmp=0;
        }
        return cmp;
    }
}
