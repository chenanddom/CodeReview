package com.itdom.sort.compelex;

import java.util.Arrays;

public class InsertTest {

    private static void sort(Comparable[] arr){
        for (int i=1;i<arr.length;i++){
            for (int j=i;j>0;j--){
                if (less(arr[j],arr[j-1])){
                    exchange(arr,j,j-1);
                }
            }
        }
    }

    private static boolean less(Comparable v,Comparable w){
        return v.compareTo(w)<0;
    }

    private static void exchange(Comparable[] arr,int i,int j){
        Comparable temp = arr[i];
        arr[i]=arr[j];
        arr[j]=temp;
    }

    public static void main(String[] args) {
            Integer[] arr = {5,3,4,2,6,7,9,8,1};
            InsertTest.sort(arr);
        System.out.println(Arrays.asList(arr));
    }
}
