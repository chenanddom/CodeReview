package com.itdom.uf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Traffic_Project_Test {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Traffic_Project_Test.class.getResourceAsStream("traffic_project.txt")));
        //获取城市数目，处死话并查集
        int number = Integer.parseInt(bufferedReader.readLine());
        UF_Tree_Weighted uf = new UF_Tree_Weighted(number);
        int roadNumber = Integer.parseInt(bufferedReader.readLine());
        for (int i = 0; i < roadNumber; i++) {
            String line = bufferedReader.readLine();
            int p = Integer.parseInt(line.split(" ")[0]);
            int q = Integer.parseInt(line.split(" ")[1]);
            uf.union(p,q);
        }
        //获取剩余的分组数量-1就是需要建设的
        int groupNumber = uf.count();
        System.out.println("还需要修"+(groupNumber-1)+"条路");

    }
}
