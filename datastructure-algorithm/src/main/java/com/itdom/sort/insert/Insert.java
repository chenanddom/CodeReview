package com.itdom.sort.insert;

public class Insert {
    public static void sort(Comparable[] arr) {
        for (int i=1;i<arr.length;i++){
            for (int j=i;j>0;j--){
                if (greater(arr[j-1],arr[j])){
                    exchange(arr,j-1,j);
                }
            }
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
