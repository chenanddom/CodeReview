package com.itdom.sort.select;

public class Select {

    public static void sort(Comparable[] arr) {
        for (int i=0;i<arr.length-2;i++){
            int minIndex=i;
            for (int j=i+1;j<arr.length;j++){
                if (greater(arr[minIndex],arr[j])){
                    minIndex=j;
                }
            }
            exchange(arr,i,minIndex);
        }



    }
    public static boolean greater(Comparable e1, Comparable e2){
        return e1.compareTo(e2) > 0;
    }
    public static void exchange(Comparable[] arr, int i, int j){
        Comparable temp = 0;
        temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
