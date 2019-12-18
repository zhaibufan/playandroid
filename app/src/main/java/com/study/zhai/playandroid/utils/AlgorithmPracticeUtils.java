package com.study.zhai.playandroid.utils;

import java.util.Arrays;

public class AlgorithmPracticeUtils {

    public static void main(String[] args) {
        int[] array = new int[]{5, 8, 6, 3, 9, 2, 1, 7};
        int[] array2 = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        int[] array3 = new int[]{3, 4, 2, 1, 5, 6, 7, 8};
//        bubbleSort(array);
//        bubbleSort2(array2);
//        bubbleSort3(array3);
//        System.out.println("result = " + Arrays.toString(array3));

//        selectedSort(array);
//        System.out.println("result = " + Arrays.toString(array));

        insertSort(array);
        System.out.println("result = " + Arrays.toString(array));
    }

    /*************************************冒泡排序(升序)*****************************************/
    /**
     * 冒泡排序第一版
     * 无任何优化，每一元素和下一个元素进行比较和交换，较大的元素向右侧移动
     * 时间复杂度n平方
     *
     * @param array
     */
    private static void bubbleSort(int[] array) {
        int temp;
        int length = array.length;
        for (int i = 0; i < length; i++) { //排序的轮数
            for (int j = 0; j < length - i - 1; j++) { //数组元素两两冒泡比较，确定有序区的长度
                if (array[j] > array[j + 1]) {
                    temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
                System.out.println("i = " + i + " j = " + j);
            }
        }
    }

    /**
     * 冒泡排序第二版
     * 如果我们能判断出数列已经有序，并且做出标记，剩下的几轮排序就可以不必执行，提早结束工作
     *
     * @param array
     */
    private static void bubbleSort2(int[] array) {
        int temp;
        int length = array.length;
        for (int i = 0; i < length; i++) {
            //有序标记，每一轮的初始是true。true代表有序，false无序
            boolean isSort = true;
            for (int j = 0; j < length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    //有元素交换，所以不是有序，标记变为false
                    isSort = false;
                }
                System.out.println("i = " + i + " j = " + j);
            }
            if (isSort) {
                break;
            }
        }
    }

    /**
     * 冒泡排序第三版
     * 按照上面两版的逻辑，有序区的长度和排序的轮数是相等的。比如第一轮排序过后的有序区长度是1，
     * 第二轮排序过后的有序区长度是2....,实际上，数列真正的有序区可能会大于这个长度,例如{3, 4, 2, 1, 5, 6, 7, 8}
     * 这种数组，后面4个元素本身就是升序的，所以后面的许多次元素比较是没有意义的
     *
     * @param array
     */
    private static void bubbleSort3(int[] array) {
        int temp;
        int length = array.length;
        //记录最后一次交换的位置
        int lastExchangeIndex = 0;
        //无序数列的边界，每次比较只需要比到这里为止
        int sortBorder = length - 1;
        for (int i = 0; i < length; i++) {
            boolean isSort = true;
            for (int j = 0; j < sortBorder; j++) {
                if (array[j] > array[j + 1]) {
                    temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    isSort = false;
                    //把无序数列的边界更新为最后一次交换元素的位置
                    lastExchangeIndex = j;
                }
                System.out.println("i = " + i + " j = " + j);
            }
            sortBorder = lastExchangeIndex;
            if (isSort) {
                break;
            }
        }
    }


    /*************************************选择排序(升序)*****************************************/
    /**
     * 选择排序
     * 每轮找出最小元素放到最前面
     * 相比与冒泡排序，选择排序不用每次交换，有交换次数少优点，但有一个不稳定性问题，当数组中包含多个相等的
     * 元素时，选择排序可能打乱他们原有顺序，而反观冒泡至少是没有这个问题，所以是一个稳定排序
     * 时间复杂度n平方
     *
     * @param arrays
     */
    private static void selectedSort(int[] arrays) {
        int temp;
        int length = arrays.length;
        for (int i = 0; i < length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < length; j++) {
                minIndex = arrays[minIndex] > arrays[j] ? j : minIndex;
            }
            temp = arrays[minIndex];
            arrays[minIndex] = arrays[i];
            arrays[i] = temp;
        }
    }


    /*************************************插入排序(升序)*****************************************/
    /**
     * 插入排序
     * 构建有序序列，对于未排序数据，在已排序序列中从后向前扫描，找到相应位置并插入。插入排序在实现上，
     * 在从后向前扫描过程中，需要反复把已排序元素逐步向后挪位，为最新元素提供插入空间。
     * 时间复杂度n平方
     *
     * @param arrays
     */
    private static void insertSort(int[] arrays) {
        for (int i = 1; i < arrays.length; i++) {
            int insertValue = arrays[i];
            int j;
            for (j = i; j > 0 && insertValue < arrays[j-1]; j--) {
                arrays[j] = arrays[j-1];
            }
            arrays[j] = insertValue;
        }
    }
}
