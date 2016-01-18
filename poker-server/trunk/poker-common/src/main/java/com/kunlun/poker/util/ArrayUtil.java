package com.kunlun.poker.util;



public class ArrayUtil {

	public static <T> void each(T[] objs, int index1, int index2) {
		if (index1 == index2) {
			return;
		}
		T key = objs[index1];
		objs[index1] = objs[index2];
		objs[index2] = key;
	}

	public static void each(float[] numbers, int index1, int index2) {
		if (index1 == index2) {
			return;
		}
		float key = numbers[index1];
		numbers[index1] = numbers[index2];
		numbers[index2] = key;
	}

	public static void each(short[] numbers, int index1, int index2) {
		if (index1 == index2) {
			return;
		}
		short key = numbers[index1];
		numbers[index1] = numbers[index2];
		numbers[index2] = key;
	}

	public static void each(int[] numbers, int index1, int index2) {
		if (index1 == index2) {
			return;
		}
		int key = numbers[index1];
		numbers[index1] = numbers[index2];
		numbers[index2] = key;
	}

	public static <T extends Comparable<T>> void selectionSort(T[] objects) {
		selectionSort(objects, 0, objects.length);
	}

	public static <T extends Comparable<T>> void selectionSort(T[] objects, int from, int length) {
		for (int sorted = from; sorted < from + length - 1; sorted ++) {
			int smallist = sorted;

			for (int j = sorted + 1; j < from + length; j++) {
				if (objects[smallist].compareTo(objects[j]) > 0) {
					smallist = j;
				}
			}

			ArrayUtil.each(objects, sorted, smallist);
		}
	}

	public static <T> void reverse(T[] objects) {
		int length = objects.length;
		for (int i = 0; i < length / 2; i++) {
			each(objects, i, length - i - 1);
		}
	}

	public static boolean nextCombinationIndexes(int[] indexes, int total) {
		int r = indexes.length;
		int i = r - 1;
		while (indexes[i] == total - r + i) {
			if(i == 0)
				return false;
			i --;
		}

		indexes[i] = indexes[i] + 1;

		for (int j = i + 1; j < r; j++) {
			indexes[j] = indexes[i] + j - i;
		}

		return true;
	}
}
