package com.str.sort;

public class SortUtil {
    // 插入排序
    public static void insertSort(int[] a) {
        int len = a.length;
        int insertNum, j;
        for (int i = 1; i < len; i++) {
            insertNum = a[i];
            j = i - 1;
            while (j >= 0 && insertNum < a[j]) {
                a[j+1] = a[j];
                j--;
            }
            a[j+1] = insertNum;
        }
    }

    // 希尔排序（思路不清晰，丢弃）
    public static void sheelSortDiscard(int[] a){
        for (int gap = a.length/2; gap > 0; gap /= 2) {
            for (int i = gap; i < a.length; i++) {
                int j = i;
                int temp = a[j];
                if (a[j] < a[j-gap]) {
                    while (j-gap >= 0 && temp < a[j-gap]) {
                        a[j] = a[j-gap];
                        j -= gap;
                    }
                    a[j] = temp;
                }
            }
        }
    }

    // 希尔排序（采用）（三层for + if）
    public static void sheelSort(int[] a) {
        for (int d = a.length/2; d > 0; d /= 2) {
            for (int i = d; i < a.length; i++) {
                for (int j = i-d; j >= 0; j -= d) {
                    if (a[j] > a[j+d]) {
                        // 交换元素
                        int temp = a[j];
                        a[j] = a[j+d];
                        a[j+d] = temp;
                    }
                }
            }
        }
    }

    // 简单选择排序
    public static void selectSort(int[] a) {
        for (int i = 0; i < a.length-1; i++) {
            // 筛查最小值，并保存其索引（猴子下山算法）
            int min = a[i];
            int index = i;
            for (int j = i+1; j < a.length; j++) {
                if (a[j] < min) {
                    min = a[j];
                    index = j;
                }
            }
            // 将最小值交换至当前位置
            a[index] = a[i];
            a[i] = min;
        }
    }

    // 堆排序
    public static void heapSort(int[] a) {
        // 1.构建大顶堆
        // 从第一个非叶子节点，自下而上，从右至左，调整结构
        for (int i = a.length/2-1; i >= 0; i--) {
            adjustHeap(a, i, a.length);
        }
        // 2.将堆顶移至数组尾部，将剩余节点重新构建大顶堆
        for (int j = a.length-1; j > 0; j--) {
            // 将堆顶元素与尾元素交换
            swap(a, 0, j);
            // 重新对堆进行调整
            adjustHeap(a, 0, j);
        }
    }

    // 调整大顶堆
    private static void adjustHeap(int[] a, int i, int length) {
        // 先取出当前元素
        int temp = a[i];
        // 从 i 节点的左子节点开始， 也就是 2i+1 处开始
        for (int k = i*2+1; k < length; k = k*2+1) {
            // 如果左子节点小于右子节点，k 指向右子节点
            if (k+1 < length && a[k] < a[k+1]) {
                k++;
            }
            // 如果子节点大于父节点，将子节点值赋给父节点
            if (a[k] > temp) {
                a[i] = a[k];
                i = k;
            } else {
                break;
            }
        }
        a[i] = temp;
    }

    // 交换元素
    private static void swap(int[] a, int m, int n) {
        int temp = a[m];
        a[m] = a[n];
        a[n] = temp;
    }

    /**
     * 快速排序
     * 算法：挖坑填数法
     *     （教科书上说的是分治法，实际上，分治法只占次要地位，不提也罢）
     * 1) i=low; j=high; 将基准数挖出形成第一个坑 a[i];
     * 2) j-- 由后向前，查找比基数小的数，找到后，挖出此数，填到第一个坑 a[i] 中;
     * 3) i++ 由前向后，查找比基数大的数，找到后，挖出此数，填到前一个坑 a[j] 中；
     * 4) 重复第2、3步，直到 i=j, 将基准数填入 a[i] 中；
     *
     * */
    public static void quickSort(int[] a, int low, int high) {
        if (low < high) {
            int i = low, j = high;
            // 基数，也是第一个坑
            int x = a[low];

            // 挖坑填数
            while (i < j) {
                // 由后向前，查找小于基准的数，来填 a[i]
                while (i < j && a[j] >= x) j--;
                if (i < j) a[i++] = a[j];
                // 由前向后，查找大于基准的数，来填 a[j]
                while (i < j && a[i] < x) i++;
                if (i < j) a[j--] = a[i];
            }
            a[i] = x;

            // 递归调用，也就是教科书上说的分治法
            quickSort(a, low, i-1);
            quickSort(a, i+1, high);
        }
    }
}
