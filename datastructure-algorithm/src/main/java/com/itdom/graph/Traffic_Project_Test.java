package com.itdom.graph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 畅通过程
 */
public class Traffic_Project_Test {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(Traffic_Project_Test.class.getResourceAsStream("traffic_project.txt")));
        Integer cityNumber = Integer.parseInt(reader.readLine());
        Integer roadNumber = Integer.parseInt(reader.readLine());
        Graph graph = new Graph(cityNumber);
        for (Integer i = 0; i < roadNumber; i++) {
            String line = reader.readLine();
            int i1 = Integer.parseInt(line.split(" ")[0]);
            int i2 = Integer.parseInt(line.split(" ")[1]);
            graph.addEdge(i1,i2);
        }
        DepthFirstSearch depthFirstSearch = new DepthFirstSearch(graph, 9);
        System.out.println("8顶点和9顶点通吗?"+depthFirstSearch.marked(8));
        System.out.println("9顶点和10顶点通吗?"+depthFirstSearch.marked(10));
    }
}
