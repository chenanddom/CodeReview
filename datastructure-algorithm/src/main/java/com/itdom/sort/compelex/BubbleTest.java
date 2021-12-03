package com.itdom.sort.compelex;

import java.util.Arrays;

public class BubbleTest {

public static void sort(Comparable[] arr){
for (int i=0;i<arr.length-1;i++){
    for (int j=arr.length-1;j>0;j--){
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
        Integer[] arr = {3,2,1,6,4,7,9,8};
        BubbleTest.sort(arr);
        System.out.println(Arrays.asList(arr));
    }

}
